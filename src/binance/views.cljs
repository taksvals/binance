(ns binance.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [binance.subs :as subs]
   [binance.events :as events]
   [clojure.core.async :refer [go-loop]]
   [clojure.core.async :refer [timeout]]
   [clojure.core.async :refer [<!]]))

(def radio-value (r/atom 0))
(def grid-value (r/atom :reload-all))

(defn grid-row [{:keys [price symbol]}]
  [:div.grid {:key symbol}
   [:div.grid-symbol
    [:p symbol]]
   [:div.grid-price
    [:p price]]])

(defn grid [data]
  [:div
   [:div.grid
   [:div.grid-symbol
    [:h2 "Symbol"]]
   [:div.grid-price
    [:h2 "Price"]]]
    (map grid-row @data)
  ])

(defn main-panel []
  (let [data (re-frame/subscribe [::subs/data])]
    (fn []
      ;; (js/setInterval #(js/console.log (:symbol (first @data))) 5000)
      (go-loop [seconds 0]
        (<! (timeout 5000))
        (print "waited" seconds "seconds")
        (re-frame/dispatch [@grid-value])
        (recur (#(+ seconds 5) seconds)))
      [:div.container
       [:div
        [:div.form-toggle
         [:div.form-toggle-item.item-1
          [:input#fid-1
           {:type "radio"
            :name "pricing"
            :on-change #(reset! radio-value 0)
            :checked (= @radio-value 0)}]
          [:label {:for "fid-1"} "All pricing"]]

         [:div.form-toggle-item.item-2
          [:input#fid-2
           {:type "radio"
            :name "pricing"
            :on-change #(reset! radio-value 1)
            :checked (= @radio-value 1)}]
          [:label {:for "fid-2"} "USD"]]

         [:div.form-toggle-item.item-3
          [:input#fid-3
           {:type "radio"
            :name "pricing"
            :on-change #(reset! radio-value 2)
            :checked (= @radio-value 2)}]
          [:label {:for "fid-3"} "COIN"]]]

        (when (= @radio-value 0) (reset! grid-value :reload-all))

        (when (= @radio-value 1) (reset! grid-value :reload-usd))

        (when (= @radio-value 2) (reset! grid-value :reload-coin))]
      ;;  [:button {:on-click #(re-frame/dispatch [:reload])}
      ;;   "Reload"]
      ;;  [:h1
      ;;   "Hello from " @name]
       (grid data)])))
