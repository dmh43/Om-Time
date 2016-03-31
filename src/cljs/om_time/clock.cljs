(ns om-time.clock
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om-bootstrap.random :as r]
            [om-bootstrap.button :as b]))

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
    (render-state [_ {:keys [play-pause-events set-time-events]}]
      (dom/div
       #js {:className "clock-section"}
       (dom/div
        #js {:className "clock-container"}
        (dom/div
         #js {:className "clock"}
         (str (format-time (sec-to-min-sec sec-remaining))))
        (dom/div #js {:className "day"} (.toDateString (js/Date.)))
        (b/button-group
         {:className "input-container"}
         (b/button {:onClick (fn [e] (put! play-pause-events :play-pause))} "Start")
         (b/button {:onClick (fn [e] (put! set-time-events (* 20 60)))} "Reset")))))))
