(ns binance.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [binance.subs :as subs]))


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
    (map grid-row @data)])

(defn radio-check []
  (condp = @radio-value
    0 :reload-all
    1 :reload-usd
    2 :reload-coin))

(defn main-panel []
  (let [data (re-frame/subscribe [::subs/data])
        now (re-frame/subscribe [::subs/now])]
    (fn []
      [:div.container
       [:div.main-text
        [:h1 "Binance"]
        [:p "All prices in real time!"]]
       [:div.toggle
        [:div.form-toggle
         [:div.form-toggle-item.item-1

          [:input#fid-1
           {:type "radio"
            :name "pricing"
            :on-change #(reset! radio-value 0)
            :on-click #(re-frame/dispatch [:reload-all])
            :checked (= @radio-value 0)}]
          [:label {:for "fid-1"} "All pricing"]]

         [:div.form-toggle-item.item-2
          [:input#fid-2
           {:type "radio"
            :name "pricing"
            :on-change #(reset! radio-value 1)
            :on-click #(re-frame/dispatch [:reload-usd])
            :checked (= @radio-value 1)}]
          [:label {:for "fid-2"} "USD"]]

         [:div.form-toggle-item.item-3
          [:input#fid-3
           {:type "radio"
            :name "pricing"
            :on-change #(reset! radio-value 2)
            :on-click #(re-frame/dispatch [:reload-coin])
            :checked (= @radio-value 2)}]
          [:label {:for "fid-3"} "COIN"]]]

        [:div.reset
         (reset! grid-value (radio-check))]
        [:div]
        [:h2 @now]]
       
       (grid data)])))
