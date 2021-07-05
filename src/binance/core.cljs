(ns binance.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [binance.events :as events]
   [binance.views :as views
                  :refer [grid-value]]
   [binance.config :as config]
   [clojure.core.async :refer [go-loop 
                               timeout
                                <!]]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (go-loop [seconds 0]
        (<! (timeout 5000))
        (print "waited" seconds "seconds")
        (re-frame/dispatch [@grid-value])
        (recur (+ seconds 5)))
  (mount-root))
