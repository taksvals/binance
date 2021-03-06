(ns binance.events
  (:require
   [re-frame.core :as re-frame]
   [binance.db :as db]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]))

(re-frame/reg-cofx
  :now
  (fn [cofx _data]
    (assoc cofx :now (js/Date.))))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
 :reload-all
 [(re-frame/inject-cofx :now)]
 (fn [{:keys [db] :as cofx} _]
   {:http-xhrio {:uri "https://www.binance.com/api/v3/ticker/price"
                 :method :get
                 :timeout 10000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:process-response]
                 :on-failure [:bad-response]}
    :db (assoc db :now (:now cofx))}))

(re-frame/reg-event-fx 
 :reload-usd
 [(re-frame/inject-cofx :now)]
 (fn [{:keys [db] :as cofx} _]
   {:http-xhrio {:uri "https://www.binance.com/fapi/v1/ticker/price"
                 :method :get
                 :timeout 10000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:process-response]
                 :on-failure [:bad-response]}
    :db (assoc db :now (:now cofx))}))

(re-frame/reg-event-fx
 :reload-coin
 [(re-frame/inject-cofx :now)]
 (fn [{:keys [db] :as cofx} _]
   {:http-xhrio {:uri "https://www.binance.com/dapi/v1/ticker/price"
                 :method :get
                 :timeout 10000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:process-response]
                 :on-failure [:bad-response]}
    :db (assoc db :now (:now cofx))}))

(re-frame/reg-event-db
  :process-response
  (fn [db [_ response]]
    (assoc db :data (js->clj response))))

(re-frame/reg-event-db
  :bad-response
  (fn [_ [_ response]]
    (println response)))
