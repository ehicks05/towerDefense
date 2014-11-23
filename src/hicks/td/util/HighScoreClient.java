package hicks.td.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HighScoreClient
{
    private static final String host = "192.168.1.2";
    public static void sendScore(String score) throws IOException
    {
        Socket socket = new Socket(host, 9001);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("INSERT_SCORE\t" + score);
        Log.info(in.readLine());
        socket.close();
    }

    public static String getHighScores() throws IOException
    {
        Socket socket = new Socket(host, 9001);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("GET_HIGH_SCORES");
        String highScores = in.readLine();
        socket.close();
        return highScores;
    }
}