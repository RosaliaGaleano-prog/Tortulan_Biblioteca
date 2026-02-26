package com.alura.challenge.Tortulan_Biblioteca.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
