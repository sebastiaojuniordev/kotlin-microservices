package br.com.curso.config

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

object RedisConnection {

    private val host: String = System.getenv("REDIS_HOST") ?: "localhost"
    private val port: String = System.getenv("REDIS_PORT") ?: "6379";

    fun getConnection(): Jedis {
        val jedisPool = JedisPool(JedisPoolConfig(), host, Integer.parseInt(port))
        return jedisPool.resource
    }
}