package hicks.td.net;

import hicks.td.net.Score;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoardServer
{
    private static final int LISTEN_PORT = 9001;
    private static final Path DATABASE = Paths.get("highscores.txt");
    private static final List<Score> SCORES = new ArrayList<>();

    public static void main(String[] args) throws Exception
    {
        System.out.println("The leaderboard server is running.");

        File scoresFile = new File(DATABASE.toString());
        if (!scoresFile.exists())
            scoresFile.createNewFile();

        List<String> lines = Files.readAllLines(DATABASE);
        for (String line : lines)
            SCORES.add(new Score(line));

        try (ServerSocket listener = new ServerSocket(LISTEN_PORT))
        {
            while (true)
                new Handler(listener.accept()).start();
        }
    }

    private static class Handler extends Thread
    {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket)
        {
            this.socket = socket;
        }

        public void run()
        {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintWriter out = new PrintWriter(socket.getOutputStream(), true); PrintWriter dbWriter = new PrintWriter(new BufferedWriter(new FileWriter(DATABASE.toFile(), true))))
            {
                String message = in.readLine();

                if (message.startsWith("INSERT_SCORE"))
                {
                    synchronized (SCORES)
                    {
                        String scoreString = message.replace("INSERT_SCORE\t", "");
                        Score newScore = new Score(scoreString);
                        SCORES.add(newScore);
                        dbWriter.println(newScore.toString());
                        dbWriter.flush();
                        dbWriter.close();
                        out.println("DONE INSERTION");
                    }
                }
                if (message.startsWith("GET_HIGH_SCORES"))
                {
                    String highScoresTable = "<html><table>";
                    highScoresTable += "<tr><td align='right'></td><<td align='left'>name</td><td align='right'>wave</td><td align='right'>lives</td><td align='right'>gold</td></tr>";
                    Collections.sort(SCORES);

                    List<Score> topTen = new ArrayList<>();
                    if (SCORES.size() > 10)
                        topTen = SCORES.subList(0, 10);

                    for (int i = 0; i < topTen.size(); i++)
                    {
                        Score score = topTen.get(i);
                        String scoreRow = "<tr><td>" + (i+1) + ".</td><td>" + score.getName() + "</td><td>" + score.getWave() + "</td><td>" + score.getLives() + "</td><td>" + score.getGold() + "</td></tr>";
                        highScoresTable += scoreRow;
                    }
                    highScoresTable += "</table></html>";
                    out.println(highScoresTable);
                }
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            finally
            {
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}