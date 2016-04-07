(ns om-time.clock
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om-bootstrap.random :as r]))

(defn sec-to-min-sec
  [seconds]
  {:minutes (int (/ seconds 60))
   :seconds (mod seconds 60)})

(defn format-time
  [time]
  (str (:minutes time)
       ":"
       (when (< (:seconds time) 10) 0)
       (:seconds time)))

(defn start-stop-handler
  [play-pause-events e]
  (put! play-pause-events :play-pause))

(defn start-stop-button [{:keys [play-pause-events counting?]} owner]
  (reify
    om/IRender
    (render [_]
      (dom/div
       #js {:onClick (partial start-stop-handler play-pause-events)
            :id "start-stop"}
       (if counting?
         (r/glyphicon {:glyph "pause"})
         (r/glyphicon {:glyph "play"}))))))

(defn clock [cursor owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (om/observe owner cursor))
    om/IRender
    (render [_]
      (dom/div
       #js {:className "clock-container"}
       (dom/div
        #js {:className "clock"}
        (-> @cursor
            :sec-remaining
            sec-to-min-sec
            format-time
            str))
       (dom/div #js {:className "day"}
                (-> (js/Date.)
                    .toDateString))
       (dom/div
        #js {:className "button-container"}
        (om/build start-stop-button {:play-pause-events (get-in cursor [:events :play-pause])
                                     :counting? (get @cursor :counting?)})
        (dom/div #js {:onClick #(put! (get-in cursor [:events :set-time])
                                      (get cursor :base-time))
                      :id "reset"}
                 (r/glyphicon {:glyph "refresh"})))))))
