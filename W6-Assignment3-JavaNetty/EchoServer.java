import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;

/**
 * A class representing an EchoServer.
 * 
 * @author Meggie
 *
 */
public class EchoServer {
	
	/**
	 * A class representing a handler for handling upstream events.
	 * 
	 * The SimpleChannelUpstreamHandler class takes care of parts of the Wrapper Facade
	 * pattern here. It handles accepting data from the client and responding to the client,
	 * which would require opening, reading from, and writing to sockets.
	 * 
	 * It also represents the concrete event handler part of the Reactor Pattern.
	 * 
	 * @author Meggie
	 *
	 */
	private static class EchoServerHandler extends SimpleChannelUpstreamHandler {
		/**
		 * This function is basically an onEventReceived kind of function, like in the
		 * HTTP Event Handler kind of class discussed in the Week 6 videos.
		 */
		@Override
		public void messageReceived(ChannelHandlerContext context, MessageEvent event) {
			Channel responder = event.getChannel();
			responder.write(event.getMessage());
		}
		
		/// For logging
		@Override
		public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent e) {
			e.getCause().printStackTrace();
			e.getChannel().close();
		}
	}
	
	public static void main (String[] args) throws Exception {
		// The reactor pattern uses a single thread of control, so I use a
		// single thread executor here. The channel factory performs many of the
		// functions of the Reactor in the Reactor Pattern.
		// In some ways, it also implements the Wrapper Facade as it allows thread
		// creation without explicit OS calls.
		ChannelFactory factory = new OioServerSocketChannelFactory(
				Executors.newSingleThreadExecutor(),
				Executors.newSingleThreadExecutor());
		
		ServerBootstrap server = new ServerBootstrap(factory);
		
		// EchoServerHandler is the only handler in the pipeline.
		// Adding Handlers to the pipeline is like registering handlers with the
		// Reactor in the Reactor Pattern. The ChannelPipelineFactory acts like
		// the Acceptor part of Acceptor-Connector; separating the initialization of
		// the EchoServerHandlers from the actual handling.
		server.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new EchoServerHandler());
			}
		});
		
		// The ServerBootstrap here acts as part of the Wrapper Facade replacing
		// socket creation and connection with a simple "bind" call to a port.
		server.bind(new InetSocketAddress(8080));
	}

}
