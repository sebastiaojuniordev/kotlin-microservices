package br.com.curso.api;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.discovery.ServiceInstance;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.client.LoadBalancer;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.client.loadbalance.DiscoveryClientLoadBalancerFactory;
import io.micronaut.http.filter.OncePerRequestHttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

@Filter("/**")
public class GatewayFilter extends OncePerRequestHttpServerFilter {

    private final DiscoveryClientLoadBalancerFactory loadBalancerFactory;
    private final GatewayProperties gatewayProperties;
    private final ProxyHttpClient proxyHttpClient;

    public GatewayFilter(DiscoveryClientLoadBalancerFactory loadBalancerFactory,
                         GatewayProperties gatewayProperties, ProxyHttpClient proxyHttpClient) {
        this.loadBalancerFactory = loadBalancerFactory;
        this.gatewayProperties = gatewayProperties;
        this.proxyHttpClient = proxyHttpClient;
    }

    @Override
    protected Publisher<MutableHttpResponse<?>> doFilterOnce(HttpRequest<?> request, ServerFilterChain chain) {
        String serviceName = request.getPath().replaceAll("^/([^/]+).*$", "$1");

        if (gatewayProperties.getServices().contains(serviceName)) {
            LoadBalancer loadBalancer = loadBalancerFactory.create(serviceName);
            return Flowable.fromPublisher(loadBalancer.select())
                    .flatMap(serviceInstance -> {
                        MutableHttpRequest<?> finalRequest = prepareRequestForTarget(request, serviceInstance);
                        return proxyHttpClient.proxy(finalRequest);
                    });
        }

        return Publishers.just(HttpResponse.notFound());
    }

    private MutableHttpRequest<?> prepareRequestForTarget(HttpRequest<?> request, ServiceInstance serviceInstance) {
        return request.mutate()
                .uri(uri -> uri
                        .scheme("http")
                        .host(serviceInstance.getHost())
                        .port(serviceInstance.getPort())
                        .replacePath(request.getPath().replace("/" + serviceInstance.getId(), "")));
    }
}