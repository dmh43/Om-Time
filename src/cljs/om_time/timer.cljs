(ns om-time.timer
  (:require [om.core :as om :include-macros true]))

(defn buzzer
  []
  (.play (js/Audio. "./Tone.mp3")))

(defn stop-timer
  [cursor]
  (js/clearInterval (get @cursor :timer))
  (om/update! cursor :timer nil)
  (om/update! cursor :counting? false))

(defn on-times-up
  [cursor]
  (buzzer)
  (om/update! cursor :sec-remaining (get cursor :base-time))
  (stop-timer cursor))

(defn time-keeper
  [cursor]
  (om/transact! cursor :sec-remaining dec :update)
  (when (= 0 (get @cursor :sec-remaining))
    (on-times-up cursor)))

(defn start-timer
  [cursor]
  (let [timer (js/setInterval #(time-keeper cursor) 1000)]
    (om/update! cursor :timer timer)
    (om/update! cursor :counting? true)))

(defn start-stop-timer
  [cursor]
  (if (get @cursor :timer)
    (stop-timer cursor)
    (start-timer cursor)))
