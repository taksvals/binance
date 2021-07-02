(ns binance.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [binance.subs :as subs]
   [binance.events :as events]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        data (re-frame/subscribe [::subs/data])]
    (fn []
      (js/setInterval #(js/console.log @data) 5000)
      [:div
       [:h1
        "Hello from " @name]
       [:button {:on-click #(re-frame/dispatch [:reload])}
        "Reload"]])))
