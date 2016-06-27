(ns transmorphic.core
  (:require-macros [transmorphic.core :refer [defmorph defcomponent]]
                   [cljs.analyzer.macros :refer [no-warn]])
  (:require [om.core]
            [goog.dom :as gdom]
            [om.dom :as dom]
            [om.core :as om]
            [transmorphic.repl :refer [update-ns-source! morph-eval-str]]
            [transmorphic.event :as event :refer [extract-event-handlers
                                                  clean-handlers!]]
            [transmorphic.symbolic :refer [get-description]]
            [clojure.string :refer [join split replace]]
            [clojure.set :refer [union]]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/enable-feature! :sanity-hints :dirac)
(devtools/install!)

(def meta-focus (atom #{}))

(declare rerender! insert-component! get-world-ref refresh-scene!)

(def component-migration-queue (atom {}))
(def morph-migration-queue (atom {}))

(defprotocol IMorph?
  (morph? [x]))

(defprotocol IComponent?
  (component? [x]))

(declare morphic-read morphic-mutate universe render-morph)

(defn morphic-uuid [morph]
  (swap! universe update-in [:meta-data :uuid-state (morph :type)] inc)
  (let [uuid-state (get-in @universe [:meta-data :uuid-state])]
    (case (morph :type)
      :rectangle (str "rectangle-" (uuid-state :rectangle))
      :ellipse (str "ellipse-" (uuid-state :ellipse))
      :text (str "text-" (uuid-state :text))
      :polygon (str "polygon-" (uuid-state :polygon))
      :io (str "io-" (uuid-state :io))
      :image (str "image-" (uuid-state :image)))))

(defn filepath->ns [path]
  ; still not plattform independent
  (when path
    (let [file (-> path
                 (clojure.string/replace "/Users/robin/Development/cljs-morphic/" "")
                 (clojure.string/replace "src/cljs/" "")
                 (clojure.string/replace "test/" ""))
          lib-name (subs (clojure.string/replace file "/" ".")
                         0 (- (count file) 5))]
      (symbol (demunge lib-name)))))

(defn abstraction->ns [abstraction & {:keys [ns path]}]
  (or (get-in @universe [:meta-data :abstractions abstraction])
      (or ns (filepath->ns path))))

(defn get-abstraction-info [fn-name]
  (get-in @universe [:meta-data :abstractions fn-name]))

; DOM MAPPING

(def pi js/Math.PI)

(defn morph-class [props]
  (or (:css-class props) "Morph"))

(defn morph-style [props]
  (apply merge
      (map (fn [[prop value]]
             (case prop
                :scrollable? {:overflow "scroll"}
               :position {:position "absolute"
                          :left (:x value)
                          :top (:y value)}
                ;; more to come...
                nil)) props)))

(defn html-attributes [props ident]
  (clj->js
    (merge
     (extract-event-handlers ident props refresh-scene!)
     {:key (:id props)
      :style (assoc (morph-style props) :padding 0)
      :class-name (morph-class props)})))

(def html5TransformProperty :WebkitTransform)
(def html5TransformOriginProperty :WebkitTransformOrigin)

(defn shape-style [props]
  (apply merge
    (map (fn [[prop value]]
           (case prop
             (:scale :rotation :pivot-point)
             (let [{rotation :rotation, scale :scale, pivot :pivot-point,
                    :or {rotation 0, scale 1, pivot {:x 0, :y 0}}} props]
               {html5TransformProperty (str "rotate("
                                            (mod (/ (* rotation 180) pi) 360)
                                            "deg) scale(" scale "," scale ")")
                html5TransformOriginProperty (str (:x pivot) "px " (:y pivot) "px")})
             :visible {:visibility (if value "visible" "hidden")}
             :extent {:height (:y value)
                      :width (:x value)}
             :border-width {:borderWidth value
                            :borderStyle "solid"}
             :border-color {:borderColor value
                            :borderStyle "solid"}
             :fill {:background value}
             :drop-shadow?
             (when value {:boxShadow "0 18px 40px 10px rgba(0, 0, 0, 0.36)"
                          :WebkitTransition "box-shadow 0.5s"})
             :css value
             :border-radius {:borderRadius value}
             :opacity {:opacity value}
             :border-style {:borderStyle value}
             nil)) props)))

; RENDERING

(defprotocol IInitialize
  (initialize [self]))

(defprotocol IFinialize
  (finalize [self]))

(defprotocol IRefresh
  (refresh [self props]))

(defprotocol IDidMount
  (did-mount [self]))

(defprotocol IWillReceiveProps
  (will-receive-props [self next-props]))

(defprotocol IRender
  (render [self props submorphs render-ctx]))

(defn eval-suspended-props [world-state morph]
  (assoc morph :props
         (into {}
               (map (fn [[p v]]
                      (cond
                        (-> v :dynamic?)
                        [p (v :value)]
                        (-> v :relative?)
                        [p ((v :eval) world-state morph)]
                        :default [p v])))
               (-> morph :props))))

(defn render-morph [morph]
  (reify
    om/IDidMount
    (did-mount [self]
               (when-let [did-mount (-> morph :props :did-mount)]
                 (did-mount self)))
    om/IWillReceiveProps
    (will-receive-props [self next-props]
               (when-let [recv-props (-> morph :props :will-receive-props)]
                 (recv-props self)))
    om/IRender
    (render [_]
          (if morph
            ((-> morph :morph->html) (eval-suspended-props @universe morph))
            (dom/div #js {:id "dummy-root"})))))

(defmorph div
  [{:keys [type props submorphs]}]
  (apply dom/div #js {:style (shape-style props)}
    (om/build-all render-morph submorphs)))

(defmorph rectangle
  [{:keys [morph-id type props submorphs]}]
  (dom/div (html-attributes props morph-id)
    (apply dom/div (clj->js {:style (shape-style props)})
      (om/build-all render-morph submorphs))))

(defmorph ellipse
  [{:keys [morph-id type props submorphs]}]
  (dom/div (html-attributes props morph-id)
    (let [offset (props :extent)
          x-offset (str (/ (:x offset) 2) "px")
          y-offset (str (/ (:y offset) 2) "px")
          style (shape-style props)]
      ; shape
      (dom/div (clj->js {:style (assoc style
                                        :borderRadius
                                        (str (-> props :extent :x) "px /"
                                             (-> props :extent :y) "px")) })
        (apply dom/div  (clj->js {:style {:position "absolute",
                                          :top y-offset,
                                          :left x-offset,
                                          :marginTop "-2px",
                                          :marginLeft "-2px"}})
          (om/build-all render-morph submorphs))))))

(defmorph image
  [{:keys [morph-id type props submorphs]}]
  (apply dom/div (html-attributes props morph-id)
    (dom/div nil
      (dom/img  (clj->js {:style (shape-style props)
                          :src (props :url)})))
        (om/build-all render-morph submorphs)))

(defn text-style [props]
  (clj->js
    (apply merge
      (map (fn [[prop value]]
             (if (= prop :allow-input)
               (when (false? value) {:WebkitUserSelect "none"})
               (case prop
                  :font-family {:fontFamily value}
                  :font-size {:fontSize (str value "pt")}
                  :max-text-height {:maxHeight (str value "px")}
                  :max-text-width {:maxWidth (str value "px")}
                  :min-text-height {:minHeight (str value "px")}
                  :min-text-width {:minWidth (str value "px")}
                  :text-color {:color value}
                  nil))) props))))

(defmorph text
  [{:keys [morph-id type props submorphs]}]
  (dom/div (html-attributes props morph-id)
    (apply dom/div
      (clj->js {:style (assoc (shape-style props) :cursor "default")
                :className "Morph Text"})
      (dom/span  (clj->js {:className "visible Selection"
                           ; :content-editable (:allow-input props)
                           :style (text-style props)
                           :ref "myInput"})
        (:text-string props))
      (om/build-all render-morph submorphs))))

(defn render-io
  [{:keys [morph-id props submorphs]} owner]
  (reify
    om.core/IDidMount
    (did-mount [this]
               ((:did-mount props) this))
    om.core/IRender
    (render [this] ((:render props)))))

(defmorph io
  [args]
  (om/build render-io args))

(defmorph html
  [{:keys [morph-id props submorphs]} owner]
  (dom/div (html-attributes props morph-id)
    (apply dom/div (clj->js {:style (shape-style props)})
      (:html props)
    (om/build-all render-morph submorphs))))

(defmorph ace
  [{:keys [morph-id props submorphs]} owner]
  (dom/div (html-attributes props morph-id)
    (apply dom/div (clj->js {:style (shape-style props)})
      (dom/div
        (clj->js {:id (props :ace-id)
                  :style {:height "100%"
                          :width "100%"}
                  :className "ace"}))
      (om/build-all render-morph submorphs))))

(defmorph listmorph
  [{:keys [morph-id type props submorphs]}]
  (dom/ul (html-attributes props morph-id)
    (for [[idx submorph] submorphs]
      (dom/li #js {:key idx
                   :style {:listStyleType "none"}}
        (om/build render-morph submorph)))))

(defmorph checkbox
  [{:keys [morph-id type props submorphs]}]
  (dom/div (html-attributes props morph-id)
    (apply dom/div
      (clj->js {:style (shape-style props)})
      (dom/input
        #js {:className "toggle" :type "checkbox"
             :checked (:checked? props)
             :onChange (:on-change props)})
      (om/build-all render-morph submorphs))))

; (defmorph polygon) TBD

(declare get-ref refresh-root)

(def history-cache (atom []))

(def reverted-entities (atom {}))

(defn reverted? [x]
  (contains? @reverted-entities (-> x get-ref second)))

(defn history-count []
  (count @history-cache))

(defn revert-history! [x history-idx]
  (when-let [ref (get-ref x)]
    (swap! reverted-entities assoc (second ref) history-idx)
    (swap! universe update-in ref assoc-in [:txs :reverted]
           (-> @history-cache (get history-idx) ref :txs))
    (when-let [p-ref (:prototype x)]
      (swap! universe update-in p-ref assoc-in [:txs :reverted]
           (-> @history-cache (get history-idx) ref :txs)))
    ; (prn (-> (get-in @universe ref) (get :txs)))
    (swap! universe refresh-root false)
    ; (prn (-> (get-in @universe ref) (get :txs)))
    ))

(defn detach-cache! [x]
  (when (reverted? x)
    (swap! reverted-entities dissoc (-> x get-ref second))
    (swap! universe update-in (get-ref x) assoc-in [:txs :reverted] nil)
    (when-let [p-ref (:prototype x)]
      (swap! universe update-in p-ref assoc-in [:txs :reverted] nil))))

(defn update-dynamic-props! [morph cursor-pos]
  (doseq [[k v] (-> morph :props)]
    (when (-> v :dynamic?)
      ((v :update) morph k cursor-pos))))

(declare render-component* get-morph-tree store-morph!)

(defn expand-component [state component]
  (update-in component
             [:submorphs]
             (fn [submorphs]
               (mapv #(get-morph-tree state %) submorphs))))

(defn apply-patch [state {:keys [path value skip]}]
  ; this applies a patch consisting out of a path
  ; pointing into the position inside the global morph scene graph
  ; together with the new value behind that part of the world
  ; this can be either a different morph hierarchy, or just a primitve value
  ; changing a property etc...
  (if skip
    (let [component (get state :root-component)
         ; {:keys [abstraction source-location parent owner props]} component
          state-tracker (atom (merge state {:skip skip}))
          morph-tree (render-component* component {:state state-tracker})
          new-state (-> @state-tracker
                      (assoc :morph-tree morph-tree)
                      (dissoc :skip))]
     ; (when cache-history? (swap! history-cache conj new-state))
      new-state)
    (assoc-in state path value)))

(declare wrap-morph wrap-component)

(defn- refresh-root
  ([state]
   (refresh-root state false))
  ([state patch]
   (clean-handlers!)
   (let [component (get state :root-component)
         props (get state :root-props)
         state-tracker (atom state) ; TODO: only preserve the morphs, that are structurally altered
         morph-tree (wrap-component (component props) {:state state-tracker})
         new-state (assoc @state-tracker :morph-tree morph-tree)]
     new-state)))

(defn mark-as-derived [morph]
  (assoc morph
         :prototype [:morph/by-id (-> morph :morph-id)]
         :submorphs (into [] (map mark-as-derived (-> morph :submorphs)))))

(defn submorphs->flatten
  ([submorphs]
   (submorphs->flatten submorphs nil))
  ([submorphs {idx? :idx, :as render-ctx}]
   (letfn [(reduce-morphs [morphs [idx x]]
                          (let [idx (if (and idx? idx)
                                      (str idx? "." idx)
                                      idx)]
                            (cond
                              (or (seq? x) (vector? x))
                              (into [] (concat morphs (submorphs->flatten x (assoc render-ctx :idx idx))))
                              (morph? x)
                              (conj morphs (wrap-morph x (assoc render-ctx :idx idx)))
                              (component? x)
                              (conj morphs (wrap-component x (assoc render-ctx :idx idx)))
                              :default morphs)))]
     (into [] (reduce reduce-morphs [] (zipmap (range) submorphs))))))

(defn unwrap-submorphs
  ([submorphs render-context]
   (into []
         (remove nil?)
         (submorphs->flatten submorphs render-context))))

(defn store-morph! [state morph redefined?]
  (swap! state assoc-in [:morph/by-id (-> morph :morph-id)]
         (-> morph
           (update-in
            [:owner]
            (fn [c]
              (when c [:component/by-id (-> c :component-id)])))
           (update-in
            [:submorphs]
            #(mapv (fn [s]
                     (when s [:morph/by-id (-> s :morph-id)])) %))))
  morph)

(defn store-component! [state component redefined?]
  ; rescue local state at any rate!
  (let [id (-> component :component-id)
        {:keys [local-state reconciler]} (get-in @state [:component/by-id id])
        component (assoc component
                         :local-state (or local-state (:local-state component))
                         :reconciler (or (and (not redefined?) reconciler)
                                         (:reconciler component)))]
    ; (when redefined?
    ;   (prn "redef taken into account..")
    ;   ; (swap! transmorphic.repl/component-migration-queue dissoc id)
    ;   )
    (swap! state assoc-in [:component/by-id (-> component :component-id)]
           (-> component
             (update-in
              [:owner]
              (fn [c]
                (when c [:component/by-id (-> c :component-id)])))
             (update-in
              [:submorphs]
              #(mapv (fn [s]
                       (when s [:morph/by-id (-> s :morph-id)])) %))))
    component))

; to what extent do we need the expansion at all?
; we can pospone the manual expansion of some morphs,
; but then we ignore effect of varying props
; since we still re-evaluate each morph call with the
; current parametrization, we do not have to unwrap
; a morph entirely. we can be sure that its childen will
; be asked again to expand and so on...
(defn get-morph-tree [state morph-ref]
  (let [morph (get-in @state morph-ref)]
    (update-in morph
               [:submorphs]
               (fn [submorphs]
                 (mapv (fn [m]
                         (get-morph-tree state m))
                        submorphs)))))

(declare consolidate-component)

(defn align-with-stored [x stored owner state]
  (let [{:keys [txs source-location parent]} stored
        {:keys [reconciler]} owner
        {:keys [props added removed]} (or (:reverted txs) txs)
        align (fn [x props added removed]
                (let [align-submorph (fn [submorphs ref]
                                       (if-not (contains? removed ref)
                                         (let [x (get-morph-tree state ref)]
                                           (if (component? x)
                                                   (if-let [m (render-component*
                                                               (consolidate-component state x)
                                                               {:state state})]
                                                     (conj submorphs m)
                                                     submorphs)
                                                   (conj submorphs x)))
                                         submorphs))
                      aligned-props (merge-with (fn [inline stored]
                                                  (if (-> inline :dynamic?)
                                                    (merge inline (dissoc stored :update))
                                                    stored))
                                                (:props x) props)
                      aligned-submorphs (reduce align-submorph (or (:submorphs x) []) added)]
                  (assoc x
                         :parent parent
                         :txs txs
                         :props aligned-props
                         :submorphs aligned-submorphs
                         :dirty? (or 
                                  (not= aligned-submorphs (:submorphs x))
                                  (not= aligned-props (:props x))))))]
    (if (:active? reconciler)
        (let [{:keys [props added removed]}
              (get-in reconciler [source-location :txs])]
          (align x props added removed))
        (align x props added removed))))

(defn consolidate [x store-fn key prefix state]
  (let [id (-> x key)
        stored (get-in @state [prefix id])
        owner (if stored
                (get-in @state (:owner stored))
                (:owner x))
        redefined? (contains? @component-migration-queue id)
        removed? (and stored (not= (:parent x) (:parent stored)))
        x (when-not removed?
            (assoc x :submorphs
                   (unwrap-submorphs (:submorphs x)
                                     {:parent [prefix id]
                                      :owner owner
                                      :state state})))]
    ; if txs unchanged and props the same, skip re-rendering
    (when x
      (if (and stored (not redefined?))
        (when-not removed?
          (let [x (align-with-stored x stored owner state)]
            (store-fn state x redefined?)))
        (store-fn state (assoc x :dirty? true) redefined?)))))

(defn consolidate-component [state component]
  (consolidate component store-component!
               :component-id :component/by-id state))

(defn consolidate-morph [state morph]
  (consolidate morph store-morph!
               :morph-id :morph/by-id state))

(defn render-component* [self {:keys [state idx]}]
  (let [{:keys [local-state props submorphs parent component-id]} self
        render-ctx {:idx idx, :state state, :root? true, :parent parent, 
                    :owner (get-in @state [:component/by-id component-id])}
        self (if-let [init! (and (not local-state)
                                 (satisfies? IInitialize self))]
               (assoc self :local-state (initialize self))
               self)
        morph (transmorphic.core/render self props 
                                        (into [] (map mark-as-derived submorphs)) 
                                        render-ctx)]
    (when morph
      (update-in morph [:props] #(merge props %)))))

(defn wrap-component [component render-context]
  (let [{:keys [parent owner idx state root?]} render-context 
          component-id (->> [(second parent) (or (-> component :props :id) idx)]
                         (remove nil?)
                         (join "."))
          component (consolidate-component
                     state
                     (assoc component
                            :component-id component-id
                            :parent parent
                            :owner owner
                            :txs {:props {}
                                  :removed #{}
                                  :added []}))]
    (when component
      (if (:dirty? component)
        (render-component* component render-context)
        (do
          (prn "skip")
          (get-in @state (get-world-ref @state component)))))))

(defn wrap-morph [morph render-context]
  (let [{:keys [parent owner idx state root?]} render-context
          morph-id (->> [(second parent) (or (-> morph :props :id) idx)]
                     (remove nil?)
                     (join "."))
          morph (consolidate-morph
                 state
                 (assoc morph
                        :morph-id morph-id
                        :parent parent
                        :owner owner
                        :txs {:props {}
                              :added []
                              :removed #{}}
                        :root? root?))]
    morph))

(defn update-reconciler
  [state ref-to-changed]
  (let [{:keys [source-location owner txs]} (get-in state ref-to-changed)
        reconciler (-> state
                     (get-in owner)
                     :reconciler
                     (update-in [source-location :txs] merge txs))]
    (if owner
      (update-in state owner assoc :reconciler reconciler)
      state)))

(defn ensure
  "Ensures that the reference is
   actually valid, replacing it
   in case a redirect is referenced."
  [state ref]
  (loop [ref ref]
    (when-let [x (get-in state ref)]
      (if-let [redirect (:redirect x)]
        (recur redirect)
        ref))))

(defn become
  "Inserts a redirect into the
   identity lookup table at ref-a, that
   immediately dispatches to the
   identity ref-b"
  [state ref-a ref-b]
  (assoc-in state ref-a {:redirect ref-b}))

(defn set-props [state {:keys [ref props->values]}]
  (let [ref (ensure state ref)
        x (get-in state ref)
        prototype (-> x :prototype)
        state (cond-> state
                prototype (set-props {:ref prototype
                                      :props->values props->values})
                :update-txs (update-in ref update-in
                                     [:txs :props] merge props->values)
                :update-props (update-in ref update-in
                                         [:props] merge props->values))]
    (update-reconciler state ref)))

(defn set-prop [state {:keys [ref prop value]}]
  (set-props state {:ref ref :props->values {prop value}}))

(defn remove-morph [state {:keys [ref]}]
  (let [ref (ensure state ref)
        remove #(-> %
                  (update-in [:txs :removed]
                             conj ref)
                  (update-in [:txs :added]
                             (fn [added]
                               (into []
                                     (remove
                                      (fn [x]
                                        (= x ref))
                                      added))))
                  (update-in [:submorphs]
                             (fn [sub-refs]
                               (into []
                                     (remove (fn [x] (= x ref))
                                             sub-refs)))))
        to-be-removed (get-in state ref)
        prototype (-> to-be-removed :prototype)]
    (if prototype
      (-> (remove-morph state {:ref prototype})
        (become ref prototype))
      (-> state
        (update-in (-> to-be-removed :parent) remove)
        (update-in ref merge {:parent nil
                              :owner (when (:root? (get-in state ref))
                                       (:owner (get-in state ref)))})
        (update-reconciler (-> to-be-removed :parent))))))

(defn add-component [state {:keys [ref new-parent-ref]}]) ; obsolete?

(defn add-morph [state {:keys [ref new-parent-ref]}]
  (let [ref (ensure state ref)
        new-parent-ref (ensure state new-parent-ref)
        add #(-> %
               (update-in [:txs :added] conj ref)
               (update-in [:txs :removed] disj ref)
               (update-in [:submorphs] conj ref))
        change-parent #(assoc % :parent new-parent-ref)
        parent-prototype (-> state
                           (get-in new-parent-ref)
                           :prototype)]
    (if parent-prototype
      (add-morph state {:new-parent-ref parent-prototype
                        :ref ref})
      (-> state
        (update-in new-parent-ref add)
        (update-in ref change-parent)
        (update-reconciler new-parent-ref)))))

(defn orphanize [state {:keys [ref]}]
  (update-in state ref
             (fn [morph]
               (let [morph (dissoc morph :owner :prototype)
                     ; strip all behavior from props
                     props (into {} (remove (comp fn? val) (:props morph)))]
                 (assoc morph :props props)))))

(defn- copy-morph [state {:keys [ref new-morph-id new-id]}]
  (assoc-in state [:morph/by-id new-morph-id]
            (-> (get-in state ref)
              (assoc :morph-id new-morph-id)
              (assoc-in [:props :id] new-id))))

(defn- copy-component [state {:keys [ref new-component-id new-id]}]
  (assoc-in state [:component/by-id new-component-id]
            (-> (get-in state ref)
              (assoc :component-id new-component-id)
              (assoc-in [:props :id] new-id))))

; when moving a morph hierarchy changes are automatically
; transferred. When we move a component however, we spawn a new
; morph hierarchy, that contains different uuids, since
; every component passes its unique location as part of
; the uuids of its yielded submorphs and subcomponents.
; moving a component is therefore a different operation
; since txs need to be moved explicitly over to
; the new hierarchy of morphs.
(defn migrate-changes [state {:keys [from to]}]
  (let [from-morph (get-in state from)
        descendants (into
                     {}
                     (map
                      (fn [submorph-ref]
                        [(replace (second submorph-ref)
                                  (re-pattern (:morph-id from-morph))
                                  "") submorph-ref]))
                     (-> state (get-in from) :submorphs))
        to-morph (get-in state to)
        state (update-in state to assoc :txs (-> from-morph :txs))]
    (reduce
     (fn [state [id-postfix descendant]]
       ; strip of the parent part of the morph-id
       ; then transfer changes
       (let [co-descendant
             (some
              (fn [submorph-ref]
                (when (re-matches
                       (re-pattern (str ".*" id-postfix))
                       (second submorph-ref))
                  submorph-ref))
              (:submorphs to-morph))]
         (migrate-changes state {:from descendant
                                 :to co-descendant})))
     state descendants)))

(defn move-component [state {:keys [ref new-parent-ref]}]
  (let [ref (ensure state ref)
        new-parent-ref (ensure state new-parent-ref)
        root-morphs (into
                     #{}
                     (comp
                      (filter
                       (fn [morph-entry]
                         (when (= ref (-> morph-entry val :owner))
                           morph-entry)))
                      (map key))
                     (state :morph/by-id))
        state (-> state
                (remove-morph {:ref ref})
                (add-morph {:new-parent-ref new-parent-ref
                            :ref ref})
                (refresh-root))
        new-ref [:component/by-id (str (second new-parent-ref) "."
                                       (-> state (get-in ref) :props :id))]
        new-root-morph (->> (state :morph/by-id)
                         (some
                          (fn [[morph-id morph]]
                            (when (and (= new-ref (-> morph :owner))
                                       (-> root-morphs
                                         (contains? morph-id) not))
                              morph-id))))
        state (if new-root-morph
                (migrate-changes state {:from [:morph/by-id (first root-morphs)]
                                        :to [:morph/by-id new-root-morph]})
                state)]
    state))

(defn- move-morph [state {:keys [ref new-parent-ref]}]
  (let [ref (ensure state ref)
        new-parent-ref (ensure state new-parent-ref)]
    (-> state
      (remove-morph {:ref ref})
      (add-morph {:new-parent-ref new-parent-ref
                  :ref ref}))))

(defn set-root
  [universe root-component root-props]
  (let [state (atom universe)
        morph-tree (render-component* (root-component root-props) {:state state})]
    (assoc @state
           :morph-tree morph-tree
           :root-component root-component
           :root-props root-props)))

(defn update-namespace! [namespace-name new-source cb]
  (update-ns-source! namespace-name new-source cb)
  (swap! universe
         assoc-in
         [:namespace/by-name namespace-name] new-source))

(defn update-abstraction! [editor component {:keys [ns name new-source]}]
  (swap! component-migration-queue
         assoc (:component-id component) editor)
  (update-namespace! ns new-source
   (fn [_] (prn "Updated: " ns "/" name))))

(defn create-abstraction! [editor morph {:keys [ns name new-source]}]
  (swap! morph-migration-queue assoc (:morph-id morph)
         {:editor editor
          :new-component-name (str ns "/" name)})
  (update-namespace! ns new-source
   (fn [_] (prn "Created: " ns "/" name))))

(defn update-component [state {:keys [component-id new-local-state]}]
  (assoc-in state [:component/by-id component-id :local-state] new-local-state))

; PUBLIC TRANSFORMATION API

; this is a the queue of transactions that have been enacted
; since the last time the world was refreshed.
; emtpied after each refresh, and communicated to the server
; the server then updates its internal list of changes
; and propagates it across all clients.
; currently dirty: the server does not preserve order of txs among
; all clients, but instead they may be applied out of order
(def tx-queue (atom []))

(defn index-of
  "return the index of the supplied item, or nil"
  [v item]
  (let [len (count v)]
    (loop [i 0]
      (cond
        (<= len i)         nil,
        (= item (get v i)) i,
        :else              (recur (inc i ))))))

(defn get-world-ref 
  ([x]
   (get-world-ref @universe x))
  ([state x]
   (if (= (-> state :morph-tree :morph-id) (:morph-id x))
     [:morph-tree]
     (let [parent (get-in state (:parent x))
           idx (index-of (:submorphs parent) (get-ref state x))
           rest (get-world-ref state parent)]
       (when (and rest idx) (concat rest [:submorphs idx]))))))

(defn get-ref
  ([x]
   (get-ref @universe x))
  ([state x]
   (ensure state
           (cond
             (:morph-id x) [:morph/by-id (:morph-id x)]
             (:component-id x) [:component/by-id (:component-id x)]
             :else (throw (str "Can not transform " x))))))

(defn update-component! [component-id new-state]
  (swap! universe
         update-component
         {:component-id component-id
          :new-local-state new-state}))

(defn remove-component! [component-id]
  (om/transact! universe :component/by-id
                #(dissoc % component-id)))

(defn rerender! [self val]
  (let [id (-> self :component-id)
        state (get-in @universe
                      [:component/by-id id :local-state])]
    (update-component!
     id
     (if (fn? val)
       (val state)
       (merge state val)))))

(defn set-prop! [x prop-name prop-value]
  (swap!
   universe
   (comp (fn [state]
           (if (component? x)
             (refresh-root state)
             (refresh-root state 
                           {:path (concat (get-world-ref state x) [:props prop-name])
                            :value prop-value}))) 
         set-prop)
   {:ref (get-ref x)
    :prop prop-name
    :value prop-value})
  (get-in @universe (get-ref x)))

(defn set-props! [x props->values]
  (swap!
   universe
   (comp (fn [state]
           (if (component? x)
             (refresh-root state)
             (if-let [path (concat (get-world-ref state x) [:props])]
               (refresh-root state 
                             {:path path
                              :value (merge (get-in state path) props->values)})
               state))) set-props)
   {:ref (get-ref x)
    :props->values props->values})
  (get-in @universe (get-ref x)))

(defn refresh-scene! [& skip]
  ; refresh all morph hierarchies spanned by the entities in 'refresh'
  (swap! universe refresh-root {:skip (set skip)})
  true)

(defn move-morph! [x new-parent]
  (swap!
     universe
     (comp refresh-root move-morph)
     {:ref (get-ref x)
      :new-parent-ref (get-ref new-parent)})
  (get-in @universe (get-ref x)))

(defn orphanize! [morph]
  (swap! universe orphanize {:ref (get-ref morph)})
  (get-in @universe (get-ref morph)))

(defn remove-morph! [x]
  (swap!
   universe
   (comp refresh-root remove-morph)
   {:ref (get-ref x)}))

(defn add-component! [x component]
  (let [state @universe
        uuid (str (second (get-ref state x))
                  "." (-> component :props :id))
        link [:component/by-id uuid]
        parent-ref (get-ref state x)
        state (assoc-in state link (assoc component :component-id uuid))
        state (add-morph state {:ref link
                                :new-parent-ref parent-ref})
        state (refresh-root state)]
    (reset! universe state)
    ; find the new root-morph for that component
    (some #(when (-> % :owner (= link))
             %)
          (map #(get-in state %)
               (-> state (get-in parent-ref) :submorphs)))))

(defn move-component! [component new-parent]
  (swap! universe move-component
         {:ref (get-ref component)
          :new-parent-ref (get-ref new-parent)}))

(defn uuid-from
  [morph-id]
  (let [splitted-morph-id (split morph-id #"\.")
        id-prop (str (-> splitted-morph-id last) "-copy")]
    {:uuid (join "." (concat (butlast splitted-morph-id) (list id-prop)))
     :id id-prop}))

(defn copy-morph! [morph]
  (let [morph-id (-> morph :morph-id)
        {:keys [uuid id]} (uuid-from morph-id)]
    (swap!
     universe
     copy-morph
     {:ref (get-ref morph)
      :new-morph-id uuid
      :new-id id})
    (get-in @universe [:morph/by-id uuid])))

(defn toggle-reconciler! [component]
  (let [component-id (:component-id component)]
    (swap! universe update-in
           [:component/by-id component-id :reconciler :active?] not)))

; if the id is not changed, the new root-morph will be of the same
; id as the previous, and added twice, causing weird behavior
; if we remove the old version, both old and new will be removed
; enfore a new id automatically
(defn reload-hook [args]
  (doseq [[morph-id {:keys [editor new-component-name]}] @morph-migration-queue]
    (no-warn ; find a way for the programmer to control the current props passed to the component
             ; handle case, when a derived morph is turned into a component root
             ; here we have to turn the prototype into a component-root
             (let [morph (get-in @universe [:morph/by-id morph-id])
                   morph (if-let [p-ref (:prototype morph)]
                           (get-in @universe p-ref)
                           morph)
                   comp-fn (morph-eval-str new-component-name)
                   component (comp-fn (:props morph)) ; should be uuid
                   root-morph (add-component! (get-in @universe (:parent morph)) component)]
               (remove-morph! morph)
               (rerender! editor {:current-target root-morph})
               (prn "Morph -> Component " morph-id)
               (rerender! editor {:compiling? false
                                  :current-target root-morph}))))
  (reset! morph-migration-queue {})
  (doseq [[component editor-component] @component-migration-queue]
    (rerender! editor-component {:compiling? false})
    (prn "Wake up editor " (:component-id component))))

(def current-namespace 'cljs.user)

(defn set-root! [app-state component props target]
  (om/root
   (fn [data _]
     (reify
       om/IRender
       (render [self]
               (if-let [tree (not-empty (:morph-tree data))]
                 (om/build render-morph tree {:key :morph-id})
                 (dom/div #js {:id "dummy-root"})))))
   app-state {:target target})
  (swap! app-state set-root component props))

(def universe (atom {:morph-tree {}
                         :namespace/by-name {}
                         :abstraction/by-name {} ; ns.name -> abstraction data
                         :morph/by-id {}
                         :component/by-id {}}))
