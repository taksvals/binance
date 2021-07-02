(ns binance.events
  (:require
   [re-frame.core :as re-frame]
   [binance.db :as db]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx ;; register an event handler
 :reload           ;; for events with this name
 (fn [{db :db} _] ;; get the co-effects and destructure the event
   {:http-xhrio {:uri "https://www.binance.com/api/v3/ticker/price"
                 :method :get
                 :timeout 10000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::process-response]
                 :on-failure [::bad-response]}}))

(re-frame/reg-event-db
  ::process-response
  (fn [db [_ response]]
    (-> db
        (assoc :data (js->clj response))
        (js/console.log (:data db)))))

(re-frame/reg-event-db
  ::bad-response
  (fn [db [_ response]]
    (js/console.log "Failure")))
