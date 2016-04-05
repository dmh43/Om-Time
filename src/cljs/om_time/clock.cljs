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
  (str (:minutes time)
       ":"
       (when (< (:seconds time) 10) 0)
       (:seconds time)))

(defn start-stop-handler
  [play-pause-events cursor e]
  (put! play-pause-events :play-pause)
  (om/transact! cursor :counting? not))

(defn start-stop-button [{:keys [play-pause-events cursor]}]
  (reify
    om/IRender
    (render [_]
      (b/button
       {:onClick (partial start-stop-handler play-pause-events cursor)}
       (if (:counting? cursor)
         "Stop"
         "Start")))))

(defn clock [cursor owner]
  (reify
    om/IRender
    (render [_]
      (dom/div
       #js {:className "clock-section"}
       (dom/div
        #js {:className "clock-container"}
        (dom/div
         #js {:className "clock"}
         (do
           (js/console.log (-> cursor :sec-remaining sec-to-min-sec format-time str))
           (-> cursor
               :sec-remaining
               sec-to-min-sec
               format-time
               str)))
        (dom/div #js {:className "day"}
                 (-> (js/Date.)
                     .toDateString))
        (om/build start-stop-button {:play-pause-events (get-in cursor [:events :play-pause])
                                     :cursor cursor})
        (b/button {:onClick #(put! (get-in cursor [:events :set-time])
                                   (* 20 60))}
                  "Reset"))))))
