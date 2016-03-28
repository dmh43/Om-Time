(ns om-time.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-bootstrap.random :as r]))

(enable-console-print!)

(defonce app-state (atom {:text "Relax"}))

(defn sec-to-min-sec
  [seconds]
  {:minutes (int (/ seconds 60))
   :seconds (mod seconds 60)})

(defn format-time
  [time]
  (str (:minutes time) ":" (when (< (:seconds time) 10) 0) (:seconds time)))

(defn root-component [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:sec-remaining (* 60 20)})
    om/IDidMount
    (did-mount [_]
      (om/set-state!
       owner
       :timer
       (js/setInterval #(om/update-state! owner :sec-remaining dec) 1000)))
    om/IWillUnmount
    (will-unmount [_]
      (om/set-state!
       owner
       :timer
       (js/clearInterval (om/get-state owner :timer))))
    om/IRender
    (render [_]
      (dom/div
       nil
       (r/jumbotron
        {}
        (dom/h1 nil (:text app))
        (when (= 0 (om/get-state owner :sec-remaining)) (dom/div nil "broooo"))
        (dom/div #js {:className "time"} (format-time (sec-to-min-sec (om/get-state owner :sec-remaining)))))))))

(om/root
 root-component
 app-state
 {:target (js/document.getElementById "app")})
