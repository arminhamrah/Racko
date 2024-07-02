import acm.program.*;
import acm.graphics.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class RackoGraphicsProgram extends GraphicsProgram
{
    // constants for graphics layout
    private static final int WINDOWSIZE = 500;
    private static final int OFFSET = 20;  // offset from all edges of screen
    private static final int CARDHEIGHT = 40;
    private static final int MINCARDWIDTH = 60;

    // these are all constants for the private instance variable gameState
    private static final int GAME_OVER = -1;
    private static final int PICK_DECK_OR_DISCARD = 0;
    private static final int PICK_CARD_FROM_HAND = 1;
    private static final int WAITING_FOR_COMPUTER_MOVE = 2; 

    // private instance variables
    private RackoGame game;
    private ComputerPlayer computer;
    private int gameState;  // will be one of four constants above
    private JLabel status;  // use status.setText to provide updates of status of game
    private int replacementCardValue;  // the card drawn from either the discard pile or dec
    private GLabel deckLabel;
    public void run()
    {
        initWindow();
        initVariables();
        drawGraphics();
        playGame();
    }

    public void playGame()
    {
        while (gameState != GAME_OVER)
        {
            pause(50);
            if(gameState == WAITING_FOR_COMPUTER_MOVE)
            {
                makeComputerMove();
                gameState = PICK_DECK_OR_DISCARD;
            }
            if(game.playerWins())
            {
                gameState = GAME_OVER;
                status.setText("You won!");
            }
            if(game.computerWins())
            {
                gameState = GAME_OVER;
                status.setText("You lost!");
                status.setText("You lost. The computer hand was: "+ Arrays.toString(game.getComputerHand()));
            }
        }
    }

    public void initVariables()
    {
        game = new RackoGame();
        gameState = PICK_DECK_OR_DISCARD;
        computer = new PrettyGoodComputerPlayer();
    }

    public void initWindow()
    {
        setSize(WINDOWSIZE, WINDOWSIZE);
        setTitle("Rack-o");
        status = new JLabel("Your move! Click on deck or discard pile.");
        add(status, NORTH);
    }

    public void drawGraphics()
    {
        for (int i=0; i<10; i++)
        {
            int [] pH = game.getPlayerHand();
            GImage card = getCardForValue(pH[i]);
            card.setLocation(WINDOWSIZE/2, OFFSET+i*CARDHEIGHT);
            add(card);
        }
        deckLabel();
        discardPile();
    }

    public void deckLabel()
    {
        deckLabel = new GLabel("Deck", OFFSET, 100);
        deckLabel.setColor(Color.red);
        deckLabel.setFont("Georgia-Plain-24");
        add(deckLabel);
    }
    
    public void discardPile()
    {
        GImage discardPile = getCardForValue(game.peekAtTopCardFromDiscardPile());
        discardPile.setColor(Color.blue);
        discardPile.setLocation(OFFSET, WINDOWSIZE*1/2);
        add(discardPile);
    }
    
    public void mouseClicked(MouseEvent event)
    {
        GObject element = getElementAt(event.getX(), event.getY());
        if(gameState == GAME_OVER)
        {
            return;
        }
        if(gameState == PICK_DECK_OR_DISCARD)
        {
            if(element == null)
            {
                return;
            }
            if(element.getColor() == Color.red)
            {
                replacementCardValue = game.removeTopCardFromDeck();
                status.setText("Where do you want to put " + replacementCardValue 
                    + "? Click on a card in your hand.");
                gameState = PICK_CARD_FROM_HAND;
            }
            if(element.getColor() == Color.blue)
            {
                replacementCardValue = game.removeTopCardFromDiscardPile();
                status.setText("Where do you want to put " + replacementCardValue 
                    + "? Click on a card in your hand.");
                gameState = PICK_CARD_FROM_HAND;
            }
        }
        if(gameState == PICK_CARD_FROM_HAND)
        {
            if(element == null)
            {
                return;
            }
            if(element.getColor() == Color.red)
            {
                return;
            }
            if(element.getColor() == Color.blue)
            {
                return;
            }
            int num = getValueForCard(element);
            int index=0;
            for (int i = 0; i<game.getPlayerHand().length; i++)
            {
                if(game.getPlayerHand()[i]== num)
                    index = i;
            }
            game.makeMove(replacementCardValue ,index,game.getPlayerHand());
            updateScreen();
            status.setText("Waiting for computer to move...");
            gameState = WAITING_FOR_COMPUTER_MOVE;
        }
        if(gameState == WAITING_FOR_COMPUTER_MOVE)
        {
            return;
        }
    }

    private void makeComputerMove()
    {
        int cardValue;
        int index;
        boolean discard = computer.shouldDrawFromDiscardPile(game.peekAtTopCardFromDiscardPile(), game.getComputerHand());
        if (!discard)
        {
            cardValue = game.removeTopCardFromDeck();
            status.setText("Computer picks " + cardValue + " from the deck...");
            pause(1000);
        }
        else 
        {
            cardValue = game.removeTopCardFromDiscardPile();
            status.setText("Computer picks " + cardValue + " from the discard pile...");
            pause(1000);
        }
        index = computer.getIndexForReplacementCard(cardValue, game.getComputerHand());
        game.makeMove(cardValue, index, game.getComputerHand());
        status.setText("Computer replaces card #" + (index+1) + " in hand...");
        pause(1000);
        updateScreen();
        status.setText("Your move! Click on deck or discard pile.");
        gameState = PICK_DECK_OR_DISCARD;
    }

    private void updateScreen()
    {
        removeAll();
        drawGraphics();
    }

    private GImage getCardForValue(int cardValue)
    {
        return new GImage("card_"+cardValue+".png");
    }

    private int getValueForCard(GObject card)
    {
        return (int)(card.getWidth()-MINCARDWIDTH);
    }
}