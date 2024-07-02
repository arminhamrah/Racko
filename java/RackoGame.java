import java.util.ArrayList;
//deck.remove(0) --> returns first card (for ArrayLists)
public class RackoGame
{
    // private instance variables
    private ArrayList<Integer> deck;
    private ArrayList<Integer> discardPile;
    private int[] playerHand;
    private int[] computerHand;
    public RackoGame()
    {
        deck = new ArrayList<Integer>();
        discardPile = new ArrayList<Integer>();
        playerHand = new int[10];
        computerHand = new int[10];
        for (int i=1; i<=60; i++)
        {
            deck.add(i);
        }
        for (int i=0; i<300; i++)
        {
            int index = (int)(Math.random()*deck.size());
            int value = deck.remove(index);
            deck.add(value);
        }
        for (int i=0; i<10; i++)
        {
            playerHand[i] = deck.remove(0);
        }
        for (int i=0; i<10; i++)
        {
            computerHand[i] = deck.remove(0);
        }
        discardPile.add(deck.remove(0));
    }

    public void makeMove(int newCardValue, int handIndex, int[] hand)
    {
        discardPile.add(hand[handIndex]);
        hand[handIndex] = newCardValue;
        if(deck.size() == 1)
        {
            for (int i=1; i<=59; i++)
            {
                deck.add(i);
            }
            for (int i=0; i<300; i++)
            {
                int index = (int)(Math.random()*deck.size());
                int value = deck.remove(index);
                deck.add(value);
            }
        }
    }

    public boolean playerWins()
    {
        for (int i = 1; i<playerHand.length-1; i++)
        {
            if(playerHand[i-1]>playerHand[i])
            {
                return false;
            }
        }   
        return true;
    }

    public boolean computerWins()
    {
        for (int i = 1; i<computerHand.length-1; i++)
        {
            if(computerHand[i-1]>computerHand[i])
            {
                return false;
            }
        }   
        return true;
    }

    public int removeTopCardFromDeck()
    {
        return deck.remove(0);
    }

    public int removeTopCardFromDiscardPile()
    {
        return discardPile.remove(0);
    }

    public int peekAtTopCardFromDiscardPile()
    {
        return discardPile.get(0);
    }

    public int[] getPlayerHand()
    {
        return playerHand;
    }

    public int[] getComputerHand()
    {
        return computerHand;
    }

}
