import pandas as pd
import numpy as np

df = pd.read_csv('ventas.txt', header=None)
funcion1 = lambda x: np.random.randint(0,70)
funcion2 = lambda x: np.random.randint(0,50)

df[1] = df[1].apply(funcion1)
df[2] = df[2].apply(funcion2) 

df["Nodos"] = df[1].astype(str) + df[2].astype(str)

print(df["Nodos"].unique().shape)

df_new = df[df["Nodos"] != "00"]

df_final = df_new.drop_duplicates(subset=['Nodos'], keep='first')

df_print = df_final.drop("Nodos", axis=1)   

df_50 = df_print.sample(50)

df_100 = df_print.sample(100) 

df_500 = df_print.sample(500)

df_50.to_csv("50pedidos.txt", header=False, index=False)

df_100.to_csv("100pedidos.txt", header=False, index=False)

df_500.to_csv("500pedidos.txt", header=False, index=False)