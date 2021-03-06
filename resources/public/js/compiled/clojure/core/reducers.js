// Compiled by ClojureScript 1.7.170 {}
goog.provide('clojure.core.reducers');
goog.require('cljs.core');
goog.require('cljs.core');
clojure.core.reducers.fjtask = (function clojure$core$reducers$fjtask(f){
return f;
});
clojure.core.reducers.fjinvoke = (function clojure$core$reducers$fjinvoke(f){
return f.call(null);
});
clojure.core.reducers.fjfork = (function clojure$core$reducers$fjfork(task){
return task;
});
clojure.core.reducers.fjjoin = (function clojure$core$reducers$fjjoin(task){
return task.call(null);
});
/**
 * Like core/reduce except:
 *   When init is not provided, (f) is used.
 *   Maps are reduced with reduce-kv
 */
clojure.core.reducers.reduce = (function clojure$core$reducers$reduce(var_args){
var args30192 = [];
var len__17883__auto___30195 = arguments.length;
var i__17884__auto___30196 = (0);
while(true){
if((i__17884__auto___30196 < len__17883__auto___30195)){
args30192.push((arguments[i__17884__auto___30196]));

var G__30197 = (i__17884__auto___30196 + (1));
i__17884__auto___30196 = G__30197;
continue;
} else {
}
break;
}

var G__30194 = args30192.length;
switch (G__30194) {
case 2:
return clojure.core.reducers.reduce.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return clojure.core.reducers.reduce.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30192.length)].join('')));

}
});

clojure.core.reducers.reduce.cljs$core$IFn$_invoke$arity$2 = (function (f,coll){
return clojure.core.reducers.reduce.call(null,f,f.call(null),coll);
});

clojure.core.reducers.reduce.cljs$core$IFn$_invoke$arity$3 = (function (f,init,coll){
if(cljs.core.map_QMARK_.call(null,coll)){
return cljs.core._kv_reduce.call(null,coll,f,init);
} else {
if((coll == null)){
return init;
} else {
if(cljs.core.array_QMARK_.call(null,coll)){
return cljs.core.array_reduce.call(null,coll,f,init);
} else {
return cljs.core._reduce.call(null,coll,f,init);

}
}
}
});

clojure.core.reducers.reduce.cljs$lang$maxFixedArity = 3;

/**
 * @interface
 */
clojure.core.reducers.CollFold = function(){};

clojure.core.reducers.coll_fold = (function clojure$core$reducers$coll_fold(coll,n,combinef,reducef){
if((!((coll == null))) && (!((coll.clojure$core$reducers$CollFold$coll_fold$arity$4 == null)))){
return coll.clojure$core$reducers$CollFold$coll_fold$arity$4(coll,n,combinef,reducef);
} else {
var x__17480__auto__ = (((coll == null))?null:coll);
var m__17481__auto__ = (clojure.core.reducers.coll_fold[goog.typeOf(x__17480__auto__)]);
if(!((m__17481__auto__ == null))){
return m__17481__auto__.call(null,coll,n,combinef,reducef);
} else {
var m__17481__auto____$1 = (clojure.core.reducers.coll_fold["_"]);
if(!((m__17481__auto____$1 == null))){
return m__17481__auto____$1.call(null,coll,n,combinef,reducef);
} else {
throw cljs.core.missing_protocol.call(null,"CollFold.coll-fold",coll);
}
}
}
});

/**
 * Reduces a collection using a (potentially parallel) reduce-combine
 *   strategy. The collection is partitioned into groups of approximately
 *   n (default 512), each of which is reduced with reducef (with a seed
 *   value obtained by calling (combinef) with no arguments). The results
 *   of these reductions are then reduced with combinef (default
 *   reducef). combinef must be associative, and, when called with no
 *   arguments, (combinef) must produce its identity element. These
 *   operations may be performed in parallel, but the results will
 *   preserve order.
 * 
 *   Note: Performing operations in parallel is currently not implemented.
 */
clojure.core.reducers.fold = (function clojure$core$reducers$fold(var_args){
var args30199 = [];
var len__17883__auto___30202 = arguments.length;
var i__17884__auto___30203 = (0);
while(true){
if((i__17884__auto___30203 < len__17883__auto___30202)){
args30199.push((arguments[i__17884__auto___30203]));

var G__30204 = (i__17884__auto___30203 + (1));
i__17884__auto___30203 = G__30204;
continue;
} else {
}
break;
}

var G__30201 = args30199.length;
switch (G__30201) {
case 2:
return clojure.core.reducers.fold.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return clojure.core.reducers.fold.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return clojure.core.reducers.fold.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30199.length)].join('')));

}
});

clojure.core.reducers.fold.cljs$core$IFn$_invoke$arity$2 = (function (reducef,coll){
return clojure.core.reducers.fold.call(null,reducef,reducef,coll);
});

clojure.core.reducers.fold.cljs$core$IFn$_invoke$arity$3 = (function (combinef,reducef,coll){
return clojure.core.reducers.fold.call(null,(512),combinef,reducef,coll);
});

clojure.core.reducers.fold.cljs$core$IFn$_invoke$arity$4 = (function (n,combinef,reducef,coll){
return clojure.core.reducers.coll_fold.call(null,coll,n,combinef,reducef);
});

clojure.core.reducers.fold.cljs$lang$maxFixedArity = 4;
/**
 * Given a reducible collection, and a transformation function xf,
 *   returns a reducible collection, where any supplied reducing
 *   fn will be transformed by xf. xf is a function of reducing fn to
 *   reducing fn.
 */
clojure.core.reducers.reducer = (function clojure$core$reducers$reducer(coll,xf){
if(typeof clojure.core.reducers.t_clojure$core$reducers30209 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IReduce}
*/
clojure.core.reducers.t_clojure$core$reducers30209 = (function (reducer,coll,xf,meta30210){
this.reducer = reducer;
this.coll = coll;
this.xf = xf;
this.meta30210 = meta30210;
this.cljs$lang$protocol_mask$partition0$ = 917504;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
clojure.core.reducers.t_clojure$core$reducers30209.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_30211,meta30210__$1){
var self__ = this;
var _30211__$1 = this;
return (new clojure.core.reducers.t_clojure$core$reducers30209(self__.reducer,self__.coll,self__.xf,meta30210__$1));
});

clojure.core.reducers.t_clojure$core$reducers30209.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_30211){
var self__ = this;
var _30211__$1 = this;
return self__.meta30210;
});

clojure.core.reducers.t_clojure$core$reducers30209.prototype.cljs$core$IReduce$_reduce$arity$2 = (function (this$,f1){
var self__ = this;
var this$__$1 = this;
return cljs.core._reduce.call(null,this$__$1,f1,f1.call(null));
});

clojure.core.reducers.t_clojure$core$reducers30209.prototype.cljs$core$IReduce$_reduce$arity$3 = (function (_,f1,init){
var self__ = this;
var ___$1 = this;
return cljs.core._reduce.call(null,self__.coll,self__.xf.call(null,f1),init);
});

clojure.core.reducers.t_clojure$core$reducers30209.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"reducer","reducer",-948842876,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"coll","coll",-1006698606,null),new cljs.core.Symbol(null,"xf","xf",2042434515,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Given a reducible collection, and a transformation function xf,\n  returns a reducible collection, where any supplied reducing\n  fn will be transformed by xf. xf is a function of reducing fn to\n  reducing fn."], null)),new cljs.core.Symbol(null,"coll","coll",-1006698606,null),new cljs.core.Symbol(null,"xf","xf",2042434515,null),new cljs.core.Symbol(null,"meta30210","meta30210",-1086917402,null)], null);
});

clojure.core.reducers.t_clojure$core$reducers30209.cljs$lang$type = true;

clojure.core.reducers.t_clojure$core$reducers30209.cljs$lang$ctorStr = "clojure.core.reducers/t_clojure$core$reducers30209";

clojure.core.reducers.t_clojure$core$reducers30209.cljs$lang$ctorPrWriter = (function (this__17423__auto__,writer__17424__auto__,opt__17425__auto__){
return cljs.core._write.call(null,writer__17424__auto__,"clojure.core.reducers/t_clojure$core$reducers30209");
});

clojure.core.reducers.__GT_t_clojure$core$reducers30209 = (function clojure$core$reducers$reducer_$___GT_t_clojure$core$reducers30209(reducer__$1,coll__$1,xf__$1,meta30210){
return (new clojure.core.reducers.t_clojure$core$reducers30209(reducer__$1,coll__$1,xf__$1,meta30210));
});

}

return (new clojure.core.reducers.t_clojure$core$reducers30209(clojure$core$reducers$reducer,coll,xf,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Given a foldable collection, and a transformation function xf,
 *   returns a foldable collection, where any supplied reducing
 *   fn will be transformed by xf. xf is a function of reducing fn to
 *   reducing fn.
 */
clojure.core.reducers.folder = (function clojure$core$reducers$folder(coll,xf){
if(typeof clojure.core.reducers.t_clojure$core$reducers30215 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {clojure.core.reducers.CollFold}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IReduce}
*/
clojure.core.reducers.t_clojure$core$reducers30215 = (function (folder,coll,xf,meta30216){
this.folder = folder;
this.coll = coll;
this.xf = xf;
this.meta30216 = meta30216;
this.cljs$lang$protocol_mask$partition0$ = 917504;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
clojure.core.reducers.t_clojure$core$reducers30215.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_30217,meta30216__$1){
var self__ = this;
var _30217__$1 = this;
return (new clojure.core.reducers.t_clojure$core$reducers30215(self__.folder,self__.coll,self__.xf,meta30216__$1));
});

clojure.core.reducers.t_clojure$core$reducers30215.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_30217){
var self__ = this;
var _30217__$1 = this;
return self__.meta30216;
});

clojure.core.reducers.t_clojure$core$reducers30215.prototype.cljs$core$IReduce$_reduce$arity$2 = (function (_,f1){
var self__ = this;
var ___$1 = this;
return cljs.core._reduce.call(null,self__.coll,self__.xf.call(null,f1),f1.call(null));
});

clojure.core.reducers.t_clojure$core$reducers30215.prototype.cljs$core$IReduce$_reduce$arity$3 = (function (_,f1,init){
var self__ = this;
var ___$1 = this;
return cljs.core._reduce.call(null,self__.coll,self__.xf.call(null,f1),init);
});

clojure.core.reducers.t_clojure$core$reducers30215.prototype.clojure$core$reducers$CollFold$ = true;

clojure.core.reducers.t_clojure$core$reducers30215.prototype.clojure$core$reducers$CollFold$coll_fold$arity$4 = (function (_,n,combinef,reducef){
var self__ = this;
var ___$1 = this;
return clojure.core.reducers.coll_fold.call(null,self__.coll,n,combinef,self__.xf.call(null,reducef));
});

clojure.core.reducers.t_clojure$core$reducers30215.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"folder","folder",-1138554033,null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.list(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"coll","coll",-1006698606,null),new cljs.core.Symbol(null,"xf","xf",2042434515,null)], null))),new cljs.core.Keyword(null,"doc","doc",1913296891),"Given a foldable collection, and a transformation function xf,\n  returns a foldable collection, where any supplied reducing\n  fn will be transformed by xf. xf is a function of reducing fn to\n  reducing fn."], null)),new cljs.core.Symbol(null,"coll","coll",-1006698606,null),new cljs.core.Symbol(null,"xf","xf",2042434515,null),new cljs.core.Symbol(null,"meta30216","meta30216",1355755488,null)], null);
});

clojure.core.reducers.t_clojure$core$reducers30215.cljs$lang$type = true;

clojure.core.reducers.t_clojure$core$reducers30215.cljs$lang$ctorStr = "clojure.core.reducers/t_clojure$core$reducers30215";

clojure.core.reducers.t_clojure$core$reducers30215.cljs$lang$ctorPrWriter = (function (this__17423__auto__,writer__17424__auto__,opt__17425__auto__){
return cljs.core._write.call(null,writer__17424__auto__,"clojure.core.reducers/t_clojure$core$reducers30215");
});

clojure.core.reducers.__GT_t_clojure$core$reducers30215 = (function clojure$core$reducers$folder_$___GT_t_clojure$core$reducers30215(folder__$1,coll__$1,xf__$1,meta30216){
return (new clojure.core.reducers.t_clojure$core$reducers30215(folder__$1,coll__$1,xf__$1,meta30216));
});

}

return (new clojure.core.reducers.t_clojure$core$reducers30215(clojure$core$reducers$folder,coll,xf,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Applies f to every value in the reduction of coll. Foldable.
 */
clojure.core.reducers.map = (function clojure$core$reducers$map(var_args){
var args30218 = [];
var len__17883__auto___30221 = arguments.length;
var i__17884__auto___30222 = (0);
while(true){
if((i__17884__auto___30222 < len__17883__auto___30221)){
args30218.push((arguments[i__17884__auto___30222]));

var G__30223 = (i__17884__auto___30222 + (1));
i__17884__auto___30222 = G__30223;
continue;
} else {
}
break;
}

var G__30220 = args30218.length;
switch (G__30220) {
case 1:
return clojure.core.reducers.map.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.map.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30218.length)].join('')));

}
});

clojure.core.reducers.map.cljs$core$IFn$_invoke$arity$1 = (function (f){
return (function (x__17266__auto__){
return clojure.core.reducers.map.call(null,f,x__17266__auto__);
});
});

clojure.core.reducers.map.cljs$core$IFn$_invoke$arity$2 = (function (f,coll){
return clojure.core.reducers.folder.call(null,coll,(function (f1){
return (function() {
var G__30225 = null;
var G__30225__0 = (function (){
return f1.call(null);
});
var G__30225__2 = (function (ret,v){
return f1.call(null,ret,f.call(null,v));
});
var G__30225__3 = (function (ret,k,v){
return f1.call(null,ret,f.call(null,k,v));
});
G__30225 = function(ret,k,v){
switch(arguments.length){
case 0:
return G__30225__0.call(this);
case 2:
return G__30225__2.call(this,ret,k);
case 3:
return G__30225__3.call(this,ret,k,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30225.cljs$core$IFn$_invoke$arity$0 = G__30225__0;
G__30225.cljs$core$IFn$_invoke$arity$2 = G__30225__2;
G__30225.cljs$core$IFn$_invoke$arity$3 = G__30225__3;
return G__30225;
})()
}));
});

clojure.core.reducers.map.cljs$lang$maxFixedArity = 2;
/**
 * Applies f to every value in the reduction of coll, concatenating the result
 *   colls of (f val). Foldable.
 */
clojure.core.reducers.mapcat = (function clojure$core$reducers$mapcat(var_args){
var args30226 = [];
var len__17883__auto___30229 = arguments.length;
var i__17884__auto___30230 = (0);
while(true){
if((i__17884__auto___30230 < len__17883__auto___30229)){
args30226.push((arguments[i__17884__auto___30230]));

var G__30231 = (i__17884__auto___30230 + (1));
i__17884__auto___30230 = G__30231;
continue;
} else {
}
break;
}

var G__30228 = args30226.length;
switch (G__30228) {
case 1:
return clojure.core.reducers.mapcat.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.mapcat.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30226.length)].join('')));

}
});

clojure.core.reducers.mapcat.cljs$core$IFn$_invoke$arity$1 = (function (f){
return (function (x__17266__auto__){
return clojure.core.reducers.mapcat.call(null,f,x__17266__auto__);
});
});

clojure.core.reducers.mapcat.cljs$core$IFn$_invoke$arity$2 = (function (f,coll){
return clojure.core.reducers.folder.call(null,coll,(function (f1){
return (function() {
var G__30233 = null;
var G__30233__0 = (function (){
return f1.call(null);
});
var G__30233__2 = (function (ret,v){
return clojure.core.reducers.reduce.call(null,f1,ret,f.call(null,v));
});
var G__30233__3 = (function (ret,k,v){
return clojure.core.reducers.reduce.call(null,f1,ret,f.call(null,k,v));
});
G__30233 = function(ret,k,v){
switch(arguments.length){
case 0:
return G__30233__0.call(this);
case 2:
return G__30233__2.call(this,ret,k);
case 3:
return G__30233__3.call(this,ret,k,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30233.cljs$core$IFn$_invoke$arity$0 = G__30233__0;
G__30233.cljs$core$IFn$_invoke$arity$2 = G__30233__2;
G__30233.cljs$core$IFn$_invoke$arity$3 = G__30233__3;
return G__30233;
})()
}));
});

clojure.core.reducers.mapcat.cljs$lang$maxFixedArity = 2;
/**
 * Retains values in the reduction of coll for which (pred val)
 *   returns logical true. Foldable.
 */
clojure.core.reducers.filter = (function clojure$core$reducers$filter(var_args){
var args30234 = [];
var len__17883__auto___30237 = arguments.length;
var i__17884__auto___30238 = (0);
while(true){
if((i__17884__auto___30238 < len__17883__auto___30237)){
args30234.push((arguments[i__17884__auto___30238]));

var G__30239 = (i__17884__auto___30238 + (1));
i__17884__auto___30238 = G__30239;
continue;
} else {
}
break;
}

var G__30236 = args30234.length;
switch (G__30236) {
case 1:
return clojure.core.reducers.filter.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.filter.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30234.length)].join('')));

}
});

clojure.core.reducers.filter.cljs$core$IFn$_invoke$arity$1 = (function (pred){
return (function (x__17266__auto__){
return clojure.core.reducers.filter.call(null,pred,x__17266__auto__);
});
});

clojure.core.reducers.filter.cljs$core$IFn$_invoke$arity$2 = (function (pred,coll){
return clojure.core.reducers.folder.call(null,coll,(function (f1){
return (function() {
var G__30241 = null;
var G__30241__0 = (function (){
return f1.call(null);
});
var G__30241__2 = (function (ret,v){
if(cljs.core.truth_(pred.call(null,v))){
return f1.call(null,ret,v);
} else {
return ret;
}
});
var G__30241__3 = (function (ret,k,v){
if(cljs.core.truth_(pred.call(null,k,v))){
return f1.call(null,ret,k,v);
} else {
return ret;
}
});
G__30241 = function(ret,k,v){
switch(arguments.length){
case 0:
return G__30241__0.call(this);
case 2:
return G__30241__2.call(this,ret,k);
case 3:
return G__30241__3.call(this,ret,k,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30241.cljs$core$IFn$_invoke$arity$0 = G__30241__0;
G__30241.cljs$core$IFn$_invoke$arity$2 = G__30241__2;
G__30241.cljs$core$IFn$_invoke$arity$3 = G__30241__3;
return G__30241;
})()
}));
});

clojure.core.reducers.filter.cljs$lang$maxFixedArity = 2;
/**
 * Takes any nested combination of sequential things (lists, vectors,
 *   etc.) and returns their contents as a single, flat foldable
 *   collection.
 */
clojure.core.reducers.flatten = (function clojure$core$reducers$flatten(var_args){
var args30242 = [];
var len__17883__auto___30245 = arguments.length;
var i__17884__auto___30246 = (0);
while(true){
if((i__17884__auto___30246 < len__17883__auto___30245)){
args30242.push((arguments[i__17884__auto___30246]));

var G__30247 = (i__17884__auto___30246 + (1));
i__17884__auto___30246 = G__30247;
continue;
} else {
}
break;
}

var G__30244 = args30242.length;
switch (G__30244) {
case 0:
return clojure.core.reducers.flatten.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return clojure.core.reducers.flatten.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30242.length)].join('')));

}
});

clojure.core.reducers.flatten.cljs$core$IFn$_invoke$arity$0 = (function (){
return (function (x__17266__auto__){
return clojure.core.reducers.flatten.call(null,x__17266__auto__);
});
});

clojure.core.reducers.flatten.cljs$core$IFn$_invoke$arity$1 = (function (coll){
return clojure.core.reducers.folder.call(null,coll,(function (f1){
return (function() {
var G__30249 = null;
var G__30249__0 = (function (){
return f1.call(null);
});
var G__30249__2 = (function (ret,v){
if(cljs.core.sequential_QMARK_.call(null,v)){
return cljs.core._reduce.call(null,clojure.core.reducers.flatten.call(null,v),f1,ret);
} else {
return f1.call(null,ret,v);
}
});
G__30249 = function(ret,v){
switch(arguments.length){
case 0:
return G__30249__0.call(this);
case 2:
return G__30249__2.call(this,ret,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30249.cljs$core$IFn$_invoke$arity$0 = G__30249__0;
G__30249.cljs$core$IFn$_invoke$arity$2 = G__30249__2;
return G__30249;
})()
}));
});

clojure.core.reducers.flatten.cljs$lang$maxFixedArity = 1;
/**
 * Removes values in the reduction of coll for which (pred val)
 *   returns logical true. Foldable.
 */
clojure.core.reducers.remove = (function clojure$core$reducers$remove(var_args){
var args30250 = [];
var len__17883__auto___30253 = arguments.length;
var i__17884__auto___30254 = (0);
while(true){
if((i__17884__auto___30254 < len__17883__auto___30253)){
args30250.push((arguments[i__17884__auto___30254]));

var G__30255 = (i__17884__auto___30254 + (1));
i__17884__auto___30254 = G__30255;
continue;
} else {
}
break;
}

var G__30252 = args30250.length;
switch (G__30252) {
case 1:
return clojure.core.reducers.remove.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.remove.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30250.length)].join('')));

}
});

clojure.core.reducers.remove.cljs$core$IFn$_invoke$arity$1 = (function (pred){
return (function (x__17266__auto__){
return clojure.core.reducers.remove.call(null,pred,x__17266__auto__);
});
});

clojure.core.reducers.remove.cljs$core$IFn$_invoke$arity$2 = (function (pred,coll){
return clojure.core.reducers.filter.call(null,cljs.core.complement.call(null,pred),coll);
});

clojure.core.reducers.remove.cljs$lang$maxFixedArity = 2;
/**
 * Ends the reduction of coll when (pred val) returns logical false.
 */
clojure.core.reducers.take_while = (function clojure$core$reducers$take_while(var_args){
var args30257 = [];
var len__17883__auto___30260 = arguments.length;
var i__17884__auto___30261 = (0);
while(true){
if((i__17884__auto___30261 < len__17883__auto___30260)){
args30257.push((arguments[i__17884__auto___30261]));

var G__30262 = (i__17884__auto___30261 + (1));
i__17884__auto___30261 = G__30262;
continue;
} else {
}
break;
}

var G__30259 = args30257.length;
switch (G__30259) {
case 1:
return clojure.core.reducers.take_while.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.take_while.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30257.length)].join('')));

}
});

clojure.core.reducers.take_while.cljs$core$IFn$_invoke$arity$1 = (function (pred){
return (function (x__17266__auto__){
return clojure.core.reducers.take_while.call(null,pred,x__17266__auto__);
});
});

clojure.core.reducers.take_while.cljs$core$IFn$_invoke$arity$2 = (function (pred,coll){
return clojure.core.reducers.reducer.call(null,coll,(function (f1){
return (function() {
var G__30264 = null;
var G__30264__0 = (function (){
return f1.call(null);
});
var G__30264__2 = (function (ret,v){
if(cljs.core.truth_(pred.call(null,v))){
return f1.call(null,ret,v);
} else {
return cljs.core.reduced.call(null,ret);
}
});
var G__30264__3 = (function (ret,k,v){
if(cljs.core.truth_(pred.call(null,k,v))){
return f1.call(null,ret,k,v);
} else {
return cljs.core.reduced.call(null,ret);
}
});
G__30264 = function(ret,k,v){
switch(arguments.length){
case 0:
return G__30264__0.call(this);
case 2:
return G__30264__2.call(this,ret,k);
case 3:
return G__30264__3.call(this,ret,k,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30264.cljs$core$IFn$_invoke$arity$0 = G__30264__0;
G__30264.cljs$core$IFn$_invoke$arity$2 = G__30264__2;
G__30264.cljs$core$IFn$_invoke$arity$3 = G__30264__3;
return G__30264;
})()
}));
});

clojure.core.reducers.take_while.cljs$lang$maxFixedArity = 2;
/**
 * Ends the reduction of coll after consuming n values.
 */
clojure.core.reducers.take = (function clojure$core$reducers$take(var_args){
var args30265 = [];
var len__17883__auto___30268 = arguments.length;
var i__17884__auto___30269 = (0);
while(true){
if((i__17884__auto___30269 < len__17883__auto___30268)){
args30265.push((arguments[i__17884__auto___30269]));

var G__30270 = (i__17884__auto___30269 + (1));
i__17884__auto___30269 = G__30270;
continue;
} else {
}
break;
}

var G__30267 = args30265.length;
switch (G__30267) {
case 1:
return clojure.core.reducers.take.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.take.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30265.length)].join('')));

}
});

clojure.core.reducers.take.cljs$core$IFn$_invoke$arity$1 = (function (n){
return (function (x__17266__auto__){
return clojure.core.reducers.take.call(null,n,x__17266__auto__);
});
});

clojure.core.reducers.take.cljs$core$IFn$_invoke$arity$2 = (function (n,coll){
return clojure.core.reducers.reducer.call(null,coll,(function (f1){
var cnt = cljs.core.atom.call(null,n);
return ((function (cnt){
return (function() {
var G__30272 = null;
var G__30272__0 = (function (){
return f1.call(null);
});
var G__30272__2 = (function (ret,v){
cljs.core.swap_BANG_.call(null,cnt,cljs.core.dec);

if((cljs.core.deref.call(null,cnt) < (0))){
return cljs.core.reduced.call(null,ret);
} else {
return f1.call(null,ret,v);
}
});
var G__30272__3 = (function (ret,k,v){
cljs.core.swap_BANG_.call(null,cnt,cljs.core.dec);

if((cljs.core.deref.call(null,cnt) < (0))){
return cljs.core.reduced.call(null,ret);
} else {
return f1.call(null,ret,k,v);
}
});
G__30272 = function(ret,k,v){
switch(arguments.length){
case 0:
return G__30272__0.call(this);
case 2:
return G__30272__2.call(this,ret,k);
case 3:
return G__30272__3.call(this,ret,k,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30272.cljs$core$IFn$_invoke$arity$0 = G__30272__0;
G__30272.cljs$core$IFn$_invoke$arity$2 = G__30272__2;
G__30272.cljs$core$IFn$_invoke$arity$3 = G__30272__3;
return G__30272;
})()
;})(cnt))
}));
});

clojure.core.reducers.take.cljs$lang$maxFixedArity = 2;
/**
 * Elides the first n values from the reduction of coll.
 */
clojure.core.reducers.drop = (function clojure$core$reducers$drop(var_args){
var args30273 = [];
var len__17883__auto___30276 = arguments.length;
var i__17884__auto___30277 = (0);
while(true){
if((i__17884__auto___30277 < len__17883__auto___30276)){
args30273.push((arguments[i__17884__auto___30277]));

var G__30278 = (i__17884__auto___30277 + (1));
i__17884__auto___30277 = G__30278;
continue;
} else {
}
break;
}

var G__30275 = args30273.length;
switch (G__30275) {
case 1:
return clojure.core.reducers.drop.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.drop.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30273.length)].join('')));

}
});

clojure.core.reducers.drop.cljs$core$IFn$_invoke$arity$1 = (function (n){
return (function (x__17266__auto__){
return clojure.core.reducers.drop.call(null,n,x__17266__auto__);
});
});

clojure.core.reducers.drop.cljs$core$IFn$_invoke$arity$2 = (function (n,coll){
return clojure.core.reducers.reducer.call(null,coll,(function (f1){
var cnt = cljs.core.atom.call(null,n);
return ((function (cnt){
return (function() {
var G__30280 = null;
var G__30280__0 = (function (){
return f1.call(null);
});
var G__30280__2 = (function (ret,v){
cljs.core.swap_BANG_.call(null,cnt,cljs.core.dec);

if((cljs.core.deref.call(null,cnt) < (0))){
return f1.call(null,ret,v);
} else {
return ret;
}
});
var G__30280__3 = (function (ret,k,v){
cljs.core.swap_BANG_.call(null,cnt,cljs.core.dec);

if((cljs.core.deref.call(null,cnt) < (0))){
return f1.call(null,ret,k,v);
} else {
return ret;
}
});
G__30280 = function(ret,k,v){
switch(arguments.length){
case 0:
return G__30280__0.call(this);
case 2:
return G__30280__2.call(this,ret,k);
case 3:
return G__30280__3.call(this,ret,k,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30280.cljs$core$IFn$_invoke$arity$0 = G__30280__0;
G__30280.cljs$core$IFn$_invoke$arity$2 = G__30280__2;
G__30280.cljs$core$IFn$_invoke$arity$3 = G__30280__3;
return G__30280;
})()
;})(cnt))
}));
});

clojure.core.reducers.drop.cljs$lang$maxFixedArity = 2;

/**
* @constructor
 * @implements {clojure.core.reducers.CollFold}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IReduce}
*/
clojure.core.reducers.Cat = (function (cnt,left,right){
this.cnt = cnt;
this.left = left;
this.right = right;
this.cljs$lang$protocol_mask$partition0$ = 8912898;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
clojure.core.reducers.Cat.prototype.cljs$core$ICounted$_count$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.cnt;
});

clojure.core.reducers.Cat.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.concat.call(null,cljs.core.seq.call(null,self__.left),cljs.core.seq.call(null,self__.right));
});

clojure.core.reducers.Cat.prototype.cljs$core$IReduce$_reduce$arity$2 = (function (this$,f1){
var self__ = this;
var this$__$1 = this;
return cljs.core._reduce.call(null,this$__$1,f1,f1.call(null));
});

clojure.core.reducers.Cat.prototype.cljs$core$IReduce$_reduce$arity$3 = (function (_,f1,init){
var self__ = this;
var ___$1 = this;
return cljs.core._reduce.call(null,self__.right,f1,cljs.core._reduce.call(null,self__.left,f1,init));
});

clojure.core.reducers.Cat.prototype.clojure$core$reducers$CollFold$ = true;

clojure.core.reducers.Cat.prototype.clojure$core$reducers$CollFold$coll_fold$arity$4 = (function (this$,n,combinef,reducef){
var self__ = this;
var this$__$1 = this;
return cljs.core._reduce.call(null,this$__$1,reducef);
});

clojure.core.reducers.Cat.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"cnt","cnt",1924510325,null),new cljs.core.Symbol(null,"left","left",1241415590,null),new cljs.core.Symbol(null,"right","right",1187949694,null)], null);
});

clojure.core.reducers.Cat.cljs$lang$type = true;

clojure.core.reducers.Cat.cljs$lang$ctorStr = "clojure.core.reducers/Cat";

clojure.core.reducers.Cat.cljs$lang$ctorPrWriter = (function (this__17423__auto__,writer__17424__auto__,opt__17425__auto__){
return cljs.core._write.call(null,writer__17424__auto__,"clojure.core.reducers/Cat");
});

clojure.core.reducers.__GT_Cat = (function clojure$core$reducers$__GT_Cat(cnt,left,right){
return (new clojure.core.reducers.Cat(cnt,left,right));
});

/**
 * A high-performance combining fn that yields the catenation of the
 *   reduced values. The result is reducible, foldable, seqable and
 *   counted, providing the identity collections are reducible, seqable
 *   and counted. The single argument version will build a combining fn
 *   with the supplied identity constructor. Tests for identity
 *   with (zero? (count x)). See also foldcat.
 */
clojure.core.reducers.cat = (function clojure$core$reducers$cat(var_args){
var args30281 = [];
var len__17883__auto___30284 = arguments.length;
var i__17884__auto___30285 = (0);
while(true){
if((i__17884__auto___30285 < len__17883__auto___30284)){
args30281.push((arguments[i__17884__auto___30285]));

var G__30286 = (i__17884__auto___30285 + (1));
i__17884__auto___30285 = G__30286;
continue;
} else {
}
break;
}

var G__30283 = args30281.length;
switch (G__30283) {
case 0:
return clojure.core.reducers.cat.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return clojure.core.reducers.cat.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.core.reducers.cat.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args30281.length)].join('')));

}
});

clojure.core.reducers.cat.cljs$core$IFn$_invoke$arity$0 = (function (){
return [];
});

clojure.core.reducers.cat.cljs$core$IFn$_invoke$arity$1 = (function (ctor){
return (function() {
var G__30288 = null;
var G__30288__0 = (function (){
return ctor.call(null);
});
var G__30288__2 = (function (left,right){
return clojure.core.reducers.cat.call(null,left,right);
});
G__30288 = function(left,right){
switch(arguments.length){
case 0:
return G__30288__0.call(this);
case 2:
return G__30288__2.call(this,left,right);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__30288.cljs$core$IFn$_invoke$arity$0 = G__30288__0;
G__30288.cljs$core$IFn$_invoke$arity$2 = G__30288__2;
return G__30288;
})()
});

clojure.core.reducers.cat.cljs$core$IFn$_invoke$arity$2 = (function (left,right){
if((cljs.core.count.call(null,left) === (0))){
return right;
} else {
if((cljs.core.count.call(null,right) === (0))){
return left;
} else {
return (new clojure.core.reducers.Cat((cljs.core.count.call(null,left) + cljs.core.count.call(null,right)),left,right));

}
}
});

clojure.core.reducers.cat.cljs$lang$maxFixedArity = 2;
/**
 * .adds x to acc and returns acc
 */
clojure.core.reducers.append_BANG_ = (function clojure$core$reducers$append_BANG_(acc,x){
var G__30290 = acc;
G__30290.push(x);

return G__30290;
});
/**
 * Equivalent to (fold cat append! coll)
 */
clojure.core.reducers.foldcat = (function clojure$core$reducers$foldcat(coll){
return clojure.core.reducers.fold.call(null,clojure.core.reducers.cat,clojure.core.reducers.append_BANG_,coll);
});
/**
 * Builds a combining fn out of the supplied operator and identity
 *   constructor. op must be associative and ctor called with no args
 *   must return an identity value for it.
 */
clojure.core.reducers.monoid = (function clojure$core$reducers$monoid(op,ctor){
return (function() {
var clojure$core$reducers$monoid_$_m = null;
var clojure$core$reducers$monoid_$_m__0 = (function (){
return ctor.call(null);
});
var clojure$core$reducers$monoid_$_m__2 = (function (a,b){
return op.call(null,a,b);
});
clojure$core$reducers$monoid_$_m = function(a,b){
switch(arguments.length){
case 0:
return clojure$core$reducers$monoid_$_m__0.call(this);
case 2:
return clojure$core$reducers$monoid_$_m__2.call(this,a,b);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
clojure$core$reducers$monoid_$_m.cljs$core$IFn$_invoke$arity$0 = clojure$core$reducers$monoid_$_m__0;
clojure$core$reducers$monoid_$_m.cljs$core$IFn$_invoke$arity$2 = clojure$core$reducers$monoid_$_m__2;
return clojure$core$reducers$monoid_$_m;
})()
});
clojure.core.reducers.foldvec = (function clojure$core$reducers$foldvec(v,n,combinef,reducef){
if(cljs.core.empty_QMARK_.call(null,v)){
return combinef.call(null);
} else {
if((cljs.core.count.call(null,v) <= n)){
return clojure.core.reducers.reduce.call(null,reducef,combinef.call(null),v);
} else {
var split = cljs.core.quot.call(null,cljs.core.count.call(null,v),(2));
var v1 = cljs.core.subvec.call(null,v,(0),split);
var v2 = cljs.core.subvec.call(null,v,split,cljs.core.count.call(null,v));
var fc = ((function (split,v1,v2){
return (function (child){
return ((function (split,v1,v2){
return (function (){
return clojure$core$reducers$foldvec.call(null,child,n,combinef,reducef);
});
;})(split,v1,v2))
});})(split,v1,v2))
;
return clojure.core.reducers.fjinvoke.call(null,((function (split,v1,v2,fc){
return (function (){
var f1 = fc.call(null,v1);
var t2 = clojure.core.reducers.fjtask.call(null,fc.call(null,v2));
clojure.core.reducers.fjfork.call(null,t2);

return combinef.call(null,f1.call(null),clojure.core.reducers.fjjoin.call(null,t2));
});})(split,v1,v2,fc))
);

}
}
});
(clojure.core.reducers.CollFold["null"] = true);

(clojure.core.reducers.coll_fold["null"] = (function (coll,n,combinef,reducef){
return combinef.call(null);
}));

(clojure.core.reducers.CollFold["object"] = true);

(clojure.core.reducers.coll_fold["object"] = (function (coll,n,combinef,reducef){
return clojure.core.reducers.reduce.call(null,reducef,combinef.call(null),coll);
}));

cljs.core.PersistentVector.prototype.clojure$core$reducers$CollFold$ = true;

cljs.core.PersistentVector.prototype.clojure$core$reducers$CollFold$coll_fold$arity$4 = (function (v,n,combinef,reducef){
var v__$1 = this;
return clojure.core.reducers.foldvec.call(null,v__$1,n,combinef,reducef);
});

//# sourceMappingURL=reducers.js.map