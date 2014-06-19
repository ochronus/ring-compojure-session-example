(ns compojure_sessions.handler
(:use compojure.core
        [ring.middleware.json :only (wrap-json-response)]
        ring.middleware.session
        [ring.util.response :only (response)]
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))


(defroutes main-routes
  (GET "/" {session :session}
      (let [count (:count session 0)
            session (assoc session :count (inc count))]
       (->
         (response {:foo "Bar", :count (:count session)})
         (assoc :session session))))
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes {:session {:cookie-name "demo-session"
                      :cookie-attrs { :max-age 3600 }}})

      (wrap-base-url)
      (wrap-json-response)
      (wrap-session )))

