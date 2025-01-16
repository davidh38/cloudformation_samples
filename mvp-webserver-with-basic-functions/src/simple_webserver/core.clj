(ns simple-webserver.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as params]
            [clojure.java.io :as io]))

(defn load-template [path]
  (slurp (io/resource path)))

(defn handle-login [params]
  (let [email (get params "email")
        password (get params "password")]
    (println "Login attempt from:" email)  ; For demonstration
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (load-template "templates/dashboard.html")}))


(defn handler [request]
  (case [(:request-method request) (:uri request)]
    [:get "/"] {:status 200
                :headers {"Content-Type" "text/html"}
                :body (load-template "templates/heropage.html")}
    [:get "/login"] {:status 200
                :headers {"Content-Type" "text/html"}
                :body (load-template "templates/login.html")}
    [:post "/login"] (handle-login (:form-params request))
    {:status 404
     :headers {"Content-Type" "text/html"}
     :body "<h1>Page not found</h1>"}))

;; Start the server
(defn -main [& args]
  (println "Starting server on port 80...")
  (run-jetty (params/wrap-params handler) {:port 80 :join? false}))
