(ns simple-webserver.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as params]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]))

(defn load-template [path]
  (slurp (io/resource path)))

(defn handle-login [params]
  (let [email (get params "email")
        password (get params "password")]
      
    (println "Login attempt from:" email)
    (let [db-spec {:dbtype "postgresql"
                   :dbname "user_management"
                   :host "database-1.cluster-csc63aoyrv6e.us-east-1.rds.amazonaws.com"
                   :user "root"
                   :password "tVk1(HqC5EFR7i.bvZ7W.1j2VwMv"}
          query-result (jdbc/query db-spec
                                   ["SELECT username, hashed_password FROM users WHERE email = ?" email])]
      (println "Query result:" query-result))
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (load-template "templates/dashboard.html")}))

(defn handler [request]
  (case [(:request-method request) (:uri request)]
    [:get "/"] {:status 200
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
