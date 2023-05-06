package com.dinosaur.reactive.webclient.core.request;

/**
 * @author wanghu
 */
public class RpcBuilder {

    private  RpcContext rpcContext;

    public <T> T request() {
        return (T) RequestFacade.request(rpcContext);
    }

//    private RequestType getRequestType(RpcContext rpcContext){
//        Assert.notNull(rpcContext.getRequestEnum(),"rpc request type is null!");
//        return rpcContext.getRequestEnum();
//    }

    public RpcBuilder(Builder builder) {
        this.rpcContext = builder.rpcContext;
    }
    public static class Builder {
        private RpcContext rpcContext;

        public Builder() {
        }

        public Builder rpcContext(RpcContext rpcContext) {
            this.rpcContext = rpcContext;
            return this;
        }

        public RpcBuilder builder(){
            return new RpcBuilder(this);
        }
    }


}
