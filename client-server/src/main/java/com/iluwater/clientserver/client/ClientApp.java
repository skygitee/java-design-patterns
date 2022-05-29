package com.iluwater.clientserver.client;

import java.security.InvalidParameterException;

import com.iluwater.clientserver.server.ServerCommand;

public final class ClientApp {

    private ClientApp() { }
    /**
     * Program entry point for first client.
     *
     * @param args program runtime arguments
     */
    public static void main(final String[] args) throws Exception {
        System.out.println("ClientApp > Start Client!!!");
        // Validate Input, expecting Hostname and Port number
        String clientName = "";
        String hostname = "";
        int port = 0;
        try {
            clientName = args[0].trim();
            hostname = args[1].trim().toLowerCase();
            port = Integer.parseInt(args[2].trim());
        } catch (Exception ex) {
            showHelp();
            throw new InvalidParameterException(
                    "Client Jamie > Missing port number");
        }
        Client clientClass = new Client(clientName, hostname, port);
        clientClass.connect();
        clientClass.sendCommand(ServerCommand.Date);
        clientClass.sendCommand(ServerCommand.Version);
        clientClass.sendMultiPartCommand(new String[] {
                "This is client app.",
                " will convert to . ",
                " all Upper Case"});
        clientClass.disconnect();
    }


    private static void showHelp() {
        System.out.print("Client > java Client <hostname> <port>");
    }
}
