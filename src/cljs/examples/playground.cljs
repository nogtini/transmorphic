(ns examples.playground
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [transmorphic.core :refer [defcomponent]])
  (:require [transmorphic.symbolic :refer [format-code]]
            [om.next :as om]
            [goog.dom :as gdom]
            [transmorphic.repl :refer [init-compiler morph-defn morph-eval]]
            [transmorphic.utils :refer [add-points delta]]
            [transmorphic.morph :refer [$morph behavior]]
            [transmorphic.core :refer [revert-to-state!
                                       listmorph ellipse rectangle rerender! 
                                       image io text universe IRender IInitialize
                                       set-prop! set-root! refresh-scene! ace
                                       history-cache checkbox]]
            [transmorphic.manipulation :refer [changes-on-morph]]
            [transmorphic.event :refer [get-current-time meta-focus]]
            
            [transmorphic.tools.world :refer [world]]
            [transmorphic.tools.hand :refer [hand-morph]]
            [transmorphic.tools.window :refer [window]]
            [transmorphic.tools.function-editor :refer [component-editor]]
            [transmorphic.tools.ace]))

(defcomponent tree 
  IRender
  (render [self {:keys [stem-pos]} _]
          (rectangle
           {:fill "brown",
            :inspectable? true,
            :grabbable? true,
            :id "stub",
            :extent {:y 62, :x 18},
            :position stem-pos}
           (ellipse
            {:fill "green",
             :inspectable? true,
             :grabbable? true,
             :id "cone",
             :extent {:y 100, :x 100},
             :position {:y -80, :x -40}}))))

(defcomponent scenery 
  IRender
  (render [self props _ ]
          (rectangle
           {:fill "blue",
            :extent {:y 179, :x 274},
            :position (props :position)}
           (tree {:stem-pos {:y 95, :x 64}})
           (rectangle
            {:fill "green",
             :extent {:y 36, :x 274},
             :position {:y 143, :x 0}})
           (ellipse
            {:fill "yellow",
             :extent {:y 34, :x 37},
             :position {:y 25, :x 205}})
           (image
            {:extent {:y 101, :x 128},
             :url "http://pngimg.com/upload/cloud_PNG16.png",
             :position {:x 120, :y 9}}))))

(def PI js/Math.PI)

(defn angle-for-hour [hour]
  (* (+ -0.25 (/ hour 12)) PI 2))

(defcomponent second-pointer 
  IRender
  (render [self props _ ]
          (rectangle 
           {:id "SecondPointer"
            :position {:x 0 :y -1.5}
            :fill "red"
            :rotation (* (+ -0.25 (/ (props :seconds) 60)) 2 PI) 
            :stroke-width 2
            :extent {:x (* 0.85 (props :radius)) :y 3}})))

(defcomponent minute-pointer
  IRender
  (render [self props _]
          (rectangle {:id "MinutePointer"
                      :position {:x 0 :y -2}
                      :fill "darkblue"
                      :rotation (* (+ -0.25 (/ (props :minutes) 60)) 2 PI)
                      :stroke-width 2
                      :extent {:x (* .7 (props :radius)) :y 4}})))

(defcomponent hour-pointer 
  IRender
  (render [self props _]
          (rectangle 
           {:id "HourPointer"
            :position {:x 0 :y -2.5}
            :rotation (* (+ -0.25 (/ (props :hours) 12)) PI 2)
            :fill "darkblue"
            :stroke-width 2
            :extent {:x (* .5 (props :radius)) :y 5}})))

(defn point-from-polar [radius angle]
  {:x (* radius (.cos js/Math angle)) :y (* radius (.sin js/Math angle))})

(defcomponent hour-label 
  IRender
  (render [self props _]
          (text
           {:id (str (props :label) "h")
            :position (point-from-polar 
                       (* (props :radius) .8) 
                       (angle-for-hour (props :hour)))
            :text-string (props :label)
            :font-family "Arial"
            :allow-input false
            :font-size (props :font-size)
            :extent (or (:extent props) {:x 30 :y 30})})))

(defcomponent
  clock
  IRender
  (render
    [{:as self, :keys [local-state]} {:keys [id extent position]} _]
    (let [{:keys [time]} local-state
          radius (/ (extent :x) 2)
          {:keys [x y]} extent
          ext (if (> x y) {:y x, :x x} {:y y, :x y})]
      (ellipse
        {:id id,
         :position position,
         :extent extent,
         :step
         (fn [_]
           (rerender! self {:time (get-current-time)})
           (refresh-scene!)),
         :border-width 4,
         :border-color "darkgrey",
         :fill
         "-webkit-gradient(radial, 50% 50%, 0, 50% 50%, 250, from(rgb(255, 255, 255)), to(rgb(224, 224, 224)))",
         :pivot-point {:x 0, :y 0}}
        (map
          (fn [hour]
            (hour-label
              {:id (str hour "h"),
               :label hour,
               :hour hour,
               :radius radius,
               :scale (/ radius 15),
               :font-size 12}))
          (range 1 13))
        (hour-pointer {:radius radius, :hours (-> time :hours)})
        (minute-pointer {:radius radius, :minutes (-> time :minutes)})
        (second-pointer
          {:radius radius, :seconds (-> time :seconds)})))))














(defcomponent history-slider
  IRender
  (render
   [{:keys [local-state] :as self} 
    {:keys [width on-change position]} submorphs]
   (rectangle
    {:id "slider",
     :position position,
     :extent (or (:current-ext local-state)
                 {:x width, :y 19}),
     :fill "black",
     :pivot-point {:x 0, :y 0}}
    (ellipse
     {:id "knob",
      :wants-hand-focus? true,
      :position (or (:current-pos local-state)
                    {:x width, 
                     :y -15}),
      :extent {:x 57, :y 56},
      :draggable? true
      :on-drag-start (fn [{:keys [x y]}]
                       (rerender! self {:current-pos {:x x 
                                                      :y y}
                                        :current-ext {:x width, :y 19}}))
      :on-drag (fn [{:keys [x]}]
                 (let [{:keys [current-pos
                               current-ext
                               value]} local-state
                       new-pos (add-points current-pos {:x x :y 0})
                       new-pos (if (or (< (:x current-ext) (new-pos :x))
                                       (> 0 (new-pos :x)))
                                 current-pos
                                 new-pos)
                       new-value (:x new-pos)]
                   (rerender! self {:current-pos new-pos})
                   (when on-change (on-change self new-value)))) 
      :fill "red",
      :drop-shadow? false, 
      :pivot-point {:x 0, :y 0}}))))

(defcomponent interaction-watcher 
  IRender
  (render [self model _]
          (window {:extent (model :extent)
                   :position (model :position)
                   :title "User Interactions"
                   :scrollable? true
                   :on-resize (fn [new-extent] (rerender! self {:extent new-extent}))
                   :on-move (fn [new-pos] (rerender! self {:position new-pos}))
                   :submorphs 
                   (when-let [txs (and @meta-focus (changes-on-morph @meta-focus))]
                     (let [hl (fn [v]
                                (text {:text-string v
                                       :font-size 12
                                       :text-color "black"
                                       :font-family "Chrono Medium Italic"}))
                           entry (fn [v]
                                   (text
                                    {:font-size 12,
                                     :text-color "grey",
                                     :text-string v, 
                                     :font-family "Chrono Medium Italic"}))]
                       ; basically any visual property can be encapsueld by a widget
                       ; or part, since it can always be computet from the local state
                       ; or the props being passed. The one exception, where we actually need
                       ; to communicate visual properties between morphs, is in the case
                       ; of layouting. This is unfortunate, as it clutters the models with
                       ; unnessecary details about how morphs are supposed to be aligned.
                       ; it would be desireable to be able to ommit this, by specifying layout
                       ; properties in an entirely declarative fashion.
                       ; for example, the editor can default to always fitting its parent
                       ; and then the extent of the window part will influence the extent
                       ; of the ace editor accordingly.
                       ; We suggest a very basic variant of layouting by introducing relative props.
                       ; Relative props, are props that are evaluated with respect to the values of
                       ; other props in the immediate neighbourhood of a morph. (usually the owner)
                       [(ace {:position {:x 0 :y 0}
                              :id "model-viewer"
                              :value (format-code (-> ($morph :meta-focus) 
                                                    :model :value))
                              :line-numbers? false
                              :edited? (atom false)
                              :extent {:x 400 :y 200}} 
                             :model-viewer)
                        (listmorph {:id "structure"
                                    :position {:x 10 :y 210}}
                                   (hl "Property Changes:")
                                   (map (fn [[prop value]]
                                          (entry (str prop " " value)))
                                        (:prop-changes txs))
                                   (hl "Structural Changes:")
                                   (map (fn [tx]
                                          (entry (str (tx :op) " "
                                                      (tx :idx))))
                                        (:structure txs)))]))})))

(defcomponent scrubber
  IRender 
  (render [self m _]
          (let [k (or (m :key) :extent)
                position (m :pos)
                model (m :model)
                transform (m :transform)]
            (ellipse 
             {:fill "black"
              :id (m :id)
              :opacity 0.3
              :on-drag-start (fn [start-pos]
                               (rerender! self {:prev-cursor start-pos}))
              :on-drag (fn [new-cursor]
                         (let [{dx :x dy :y} (delta (model :prev-cursor) new-cursor)]
                           (rerender! self {:prev-cursor new-cursor
                                            k (or
                                               (when transform (transform delta (get model k))) 
                                               (add-points (get model k) 
                                                           {:x (- dx) :y (- dy)}))})))
              :position (add-points position {:x -25 :y -25})
              :extent {:x 25 :y 25} 
              :css {"cursor" "nwse-resize"}}))))

(defcomponent kitchen
  transmorphic.core/IRender
  (render [_ _ _]
          (rectangle
           {:id "kitchen-morph"
            :position {:x 50 :y 50}}
           (ellipse 
            {:id "ellipse"
             :wants-hand-focus? true
             :position {:x 100 :y 100}
             :extent {:x 100 :y 100}
             :fill "green"})
           (rectangle 
            {:id "rectangle"
             :wants-hand-focus? true
             :position {:x 300 :y 100}
             :extent {:x 100 :y 100}
             :fill "orange"})
           (text
            {:id "text"
             :position {:x 700 :y 100}
             :text-string "Text"
             :font-family "Chrono Medium Italic"
             :allow-input false
             :font-size 17
             :extent {:x 50 :y 20}})
           (checkbox {:on-change (fn [_] 
                                   (swap! transmorphic.event/stepping? not))
                      :checked? @transmorphic.event/stepping?
                      :position {:x 50 :y 50}})
            (clock {:id "Clock" 
                    :extent {:x 300 :y 300} 
                    :position {:x 300 :y 300}})
           (image {:url "http://www.daniellaondesign.com/uploads/7/3/9/7/7397659/464698_orig.jpg"
                   :extent {:x 300 :y 500}
                   :wants-hand-focus? true
                   :position {:x 300 :y 300}
                   :inspectable? true
                   :rotation 0.5
                   :id "kyoto"}))))

(defcomponent my-world
  IRender
  (render [_ _ _]
          (world {:id "world"
                  :extent {:x 1600 :y 900}
                  :mouse-move {:x 20 :y 20}}
                 (kitchen {:id "kitchen"}))))

(defonce application 
  (set-root! universe 
    (my-world {:id "foo"})
    (gdom/getElement "app")))

(def slider-state (atom {}))

; (set-root! slider-state 
;   (history-slider {:width (count @history-cache) :position {:x 700 :y 200}
;                   :on-change (fn [self value]
;                                 (revert-to-state! 
;                                 value 
;                                 [:component/by-id 
;                                   (:comoponent-id self)]))})
;   (gdom/getElement "history"))