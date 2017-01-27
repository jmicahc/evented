(ns evented_data.core
  (:require [evented_data.collections :as collections
             :refer [diff noun events]]))


(defn emap 
  ([]
   (protocols/EventMap. {} [] []))
  ([k v]
   (assoc (protocols/EventMap. {} [] []) k v))
  ([k1 v1 k2 v2]
   (assoc (protocols/EventMap. {} [] []) k1 v1 k2 v2))
  ([k1 v1 k2 v2 k3 v3]
   (assoc (protocols/EventMap. {} [] []) k1 v1 k2 v2 k3 v3))
  ([k1 v1 k2 v2 k3 v3 & kvs])
   (apply assoc (emap k1 v1 k2 v2 k3 v3) kvs))



(defn evec
  ([]
   (protocols/EventVector. [] [] []))
  ([x]
   (conj (protocols/EventVector. [] [] []) x))
  ([x y]
   (conj (protocols/EventVector. [] [] []) x y))
  ([x y & xs]
   (apply conj (protocols/EventVector. [] [] []) x y xs)))


