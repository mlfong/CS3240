package hw6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TuringMachine
{
    ArrayList<MyChar> tape;
    int head;

    /****
     * Implement the pseudo-code from (a), i.e. simulate your TM computation.
     * Your program should take input a bitstring and output ACCEPT or REJECT,
     * and in addition it should also output the sequence of tape contents at
     * each step of the computation. A TA will soon provide more detailed
     * instructions on how to submit the solution. This portion can be submitted
     * by April 18th.
     */

    /*********
     * Scan the tape and mark the first unmarked 0. If there is no unmarked 0 go
     * to step 4.
     * 
     * Continue on and mark the next unmarked 0. If there is not any, ACCEPT.
     * (b/c then we have odd number of 0's which can't be twice the number of
     * 1's). Otherwise move to the head of the tape.
     * 
     * \item Scan the tape and mark the first unmarked 1. If there is no
     * unmarked 0, ACCEPT.
     * 
     * \item Move to the head of the tape and repeat step 1.
     * 
     * \item Move to the head of the tape. Scan to see any unmarked 1s. If there
     * is, ACCEPT, else there is not, REJECT.
     */
    public TuringMachine()
    {
        this.tape = new ArrayList<MyChar>();
        this.head = 0;
    }

    public void init(String[] s)
    {
        for (String ss : s)
            this.tape.add(new MyChar(ss.charAt(0)));
    }

    public void run()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.tape.size(); i++)
            sb.append(tape.get(i).c);
        System.out.println(sb.toString());
        this.step1();
    }

    public void step1()
    {
        boolean found = false;
        for (int i = head; i < this.tape.size(); i++)
        {
            MyChar myc = this.tape.get(i);
            if (myc.isMarked() || myc.c == '1')
            {
                this.printTape();
                head++;
                continue;
            }
            else if (myc.c == '0')
            {
                myc.mark();
                found = true;
                this.printTape();
                break;
            }

        }
        if (found)
        {
            this.moveToFront();
            step2();
        }
        else
        {
            this.moveToFront();
            step4();
        }
    }

    public void step2()
    {
        boolean found = false;
        for (int i = head; i < this.tape.size(); i++)
        {
            MyChar myc = this.tape.get(i);
            if (myc.isMarked() || myc.c == '1')
            {
                this.printTape();
                head++;
                continue;
            }
            else if (myc.c == '0')
            {
                myc.mark();
                found = true;
                this.printTape();
                break;
            }

        }
        if (found)
        {
            this.moveToFront();
            step3();
        }
        else
        {
            System.out.println("ACCEPT");
            return;
        }
    }

    public void step3()
    {
        boolean found = false;
        for (int i = head; i < this.tape.size(); i++)
        {
            MyChar myc = this.tape.get(i);
            if (myc.isMarked() || myc.c == '0')
            {
                this.printTape();
                head++;
                continue;
            }
            else if (myc.c == '1')
            {
                myc.mark();
                found = true;
                this.printTape();
                break;
            }

        }
        if (found)
        {
            this.moveToFront();
            step1();
        }
        else
        {
            System.out.println("ACCEPT");
            return;
        }
    }

    public void step4()
    {
        boolean found = false;
        for (int i = head; i < this.tape.size(); i++)
        {
            MyChar myc = this.tape.get(i);
            if (!myc.isMarked() && myc.c == '1')
            {
                found = true;
                break;
            }
            else
                continue;
            
        }
        if (found)
        {
            System.out.println("ACCEPT");
        }
        else
        {
            System.out.println("REJECT");
            return;
        }
    }

    public void moveToFront()
    {
        while(head != 0){
            head--;
            printTape();
        }
    }

    public void printTape()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("$");
        for (int i = 0; i < tape.size(); i++)
        {
            if (i == head)
                sb.append("H");
            sb.append(tape.get(i));
        }
        sb.append("#");
        System.out.println(sb);
    }

    public static void main(String[] args) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File(
                "input.txt")));
        int numProbs = Integer.parseInt(br.readLine());
        for (int i = 0; i < numProbs; i++)
        {
            String s = br.readLine();
            String[] s_arr = s.split(" ");
            // }
            TuringMachine tm = new TuringMachine();
            tm.init(s_arr);
            System.out.println("Bitstring " + (1 + i) + ":");
            
            tm.run();
            System.out.println();
        }
        br.close();
    }

    class MyChar
    {
        public char c;
        public boolean marked;

        public MyChar(char c)
        {
            this(c, false);
        }

        public MyChar(char c, boolean m)
        {
            this.c = c;
            this.marked = m;
        }

        public void mark()
        {
            this.marked = true;
        }

        public boolean isMarked()
        {
            return this.marked;
        }

        public String toString()
        {
            if (!marked)
                return "" + c;
            else
                return "{" + c + "}";
        }
    }

}
