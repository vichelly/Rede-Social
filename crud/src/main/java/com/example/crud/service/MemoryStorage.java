package com.example.crud.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.example.crud.model.MessageDTO;
import com.example.crud.model.PostDTO;

public class MemoryStorage {

    // postagens por usuário
    public static Map<String, List<PostDTO>> posts = new ConcurrentHashMap<>();

    // seguidores: user -> set of followers
    public static Map<String, Set<String>> followers = new ConcurrentHashMap<>();

    // mensagens privadas: destinatário -> lista de mensagens recebidas
    public static Map<String, List<MessageDTO>> privateMessages = new ConcurrentHashMap<>();
}
