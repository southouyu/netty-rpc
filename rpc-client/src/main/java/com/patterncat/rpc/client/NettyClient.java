package com.patterncat.rpc.client;

import com.patterncat.rpc.common.codec.RpcDecoder;
import com.patterncat.rpc.common.codec.RpcEncoder;
import com.patterncat.rpc.common.dto.RpcRequest;
import com.patterncat.rpc.common.dto.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class NettyClient implements IClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup workerGroup;
    private Channel channel;

    private ClientRpcHandler clientRpcHandler = new ClientRpcHandler();

    private volatile boolean connected = false;

//    @Value("${client.workerGroupThreads:5}")
    int workerGroupThreads = 5;

    public void connect(InetSocketAddress socketAddress) {
        try{
            workerGroup = new NioEventLoopGroup(workerGroupThreads);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //处理分包传输问题
                                    .addLast("decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast("encoder", new LengthFieldPrepender(4, false))
                                    .addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(clientRpcHandler);
                        }
                    });
            channel = bootstrap.connect(socketAddress.getAddress().getHostAddress(), socketAddress.getPort())
                    .syncUninterruptibly()
                    .channel();
            connected = true;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            connected = false;
            reconnect(1,TimeUnit.SECONDS,socketAddress);
        }
    }

    public void reconnect(int delay,TimeUnit timeUnit,InetSocketAddress socketAddress){
        try {
            timeUnit.sleep(delay);
            if(connected){
                return ;
            }
            connect(socketAddress);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public RpcResponse syncSend(RpcRequest request) throws InterruptedException {
        System.out.println("send request:"+request);
        channel.writeAndFlush(request).sync();
        return clientRpcHandler.send(request,null);
    }

    public RpcResponse asyncSend(RpcRequest request,Pair<Long,TimeUnit> timeout) throws InterruptedException {
        channel.writeAndFlush(request);
        return clientRpcHandler.send(request,timeout);
    }

    public InetSocketAddress getRemoteAddress() {
        SocketAddress remoteAddress = channel.remoteAddress();
        if (!(remoteAddress instanceof InetSocketAddress)) {
            throw new RuntimeException("Get remote address error, should be InetSocketAddress");
        }
        return (InetSocketAddress) remoteAddress;
    }

    public boolean isConnected() {
        return connected;
    }

    @PreDestroy
    public void close() {
        logger.info("destroy client resources");
        if (null == channel) {
            logger.error("channel is null");
        }
        connected = false;
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        workerGroup = null;
        channel = null;
    }
}
