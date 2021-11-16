import ast
import pprint
import numpy as np
filenames = ["archivo" + str(i) for i in range(1, 21)]
json = dict()
json["paths"] = []
for file in filenames:
    sub_json = dict()
    path = list()
    rutas = open(file, "r")
    pre_path = list()
    i = 0
    for ruta in rutas:
        ruta = ast.literal_eval(ruta)
        if i != 0:
            ruta.pop(0)
        i = i + 1
        pre_path = pre_path + ruta
    
    for node in pre_path:
        nodo = dict()
        nodo["x"] = node[0]
        nodo["y"] = node[1]
        nodo["destino"] = np.random.choice(np.arange(0, 2), p=[0.95, 0.05]) 
        path.append(nodo)
    sub_json["path"] = path 
    sub_json["startTime"] = " "
    sub_json["endTime"] = " "
    json["paths"].append(sub_json)
    
pprint.pprint(json)