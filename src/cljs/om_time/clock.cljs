(ns om-time.clock
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-bootstrap.random :as r]
            [cljs.core.async :refer [put! chan <!]]))

(defn sec-to-min-sec
  [seconds]
  {:minutes (int (/ seconds 60))
   :seconds (mod seconds 60)})

(defn format-time
  [time]
  (str (:minutes time) ":" (when (< (:seconds time) 10) 0) (:seconds time)))

(defn clock [sec-remaining owner init]
  (reify
    om/IInitState
    (init-state [_]
      (:init-state init))
    om/IRenderState
    (render-state [_ {:keys [play-pause set-time]}]
      (dom/div
       nil
       (r/jumbotron
        {}
        (dom/div nil (format-time (sec-to-min-sec sec-remaining)))
        (dom/button #js {:onClick (fn [e] (put! play-pause :play-pause))} "Start/Stop")
        (dom/button #js {:onClick (fn [e] (put! set-time 1000))} "Reset!"))))))
