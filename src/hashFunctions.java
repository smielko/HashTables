// Name: Sebastian Mielko
// Class: CS 3305/Section#03
// Term: Spring 2023
// Instructor: Prof. Majeed
// Assignment: 7
import java.util.*;

public class hashFunctions
{
    public static void main(String[] args){
        int[] keys =
                {       1234, 8234, 7867, 1009, 5438, 4312, 3420, 9487, 5418, 5299,
                        5078, 8239, 1208, 5098, 5195, 5329, 4543, 3344, 7698, 5412,
                        5567, 5672, 7934, 1254, 6091, 8732, 3095, 1975, 3843, 5589,
                        5439, 8907, 4097, 3096, 4310, 5298, 9156, 3895, 6673, 7871,
                        5787, 9289, 4553, 7822, 8755, 3398, 6774, 8289, 7665, 5523};
        boolean isFinished = false;
        while(!isFinished)
        {
            int[][] HashTable = new int[50][2];
            DisplayMenu();
            Scanner myScanner = new Scanner(System.in);
            String selection = myScanner.next();
            switch (selection) {
                case "1" -> HF1(keys, HashTable); //Division Hash and Linear Probing
                case "2" -> HF2(keys, HashTable); //Division Hash and Quadratic Probing
                case "3" -> HF3(keys, HashTable); //Division Hash and Double Hashing
                case "4" -> HF4(keys, HashTable); //Chaining
                case "5" -> isFinished = true;
                default -> System.out.println("Enter valid input");
            }
        }

    }
    public static void DisplayMenu() //menu display
    {
        System.out.print("\n-----MAIN MENU--------------------------------------\n" +
                "1. Run HF1 (Division method with Linear Probing)\n" +
                "2. Run HF2 (Division method with Quadratic Probing)\n" +
                "3. Run HF3 (Division method with Double Hashing)\n" +
                "4. Run HF4 (Student Designed HF)\n" +
                "5. Exit program\n" +
                "Enter option number:");
    }
    public static void HF1(int[] keys, int[][] HashTable)
    {
        int hashedKey; int numberOfProbes = 0;
        int size = HashTable.length;
        for(int i = 0; i < size; i ++)
        {
            hashedKey = keys[i] % size; //Division Hashing
            while (HashTable[hashedKey][0] > 0) //check if empty condition
            {
                hashedKey = (keys[i] + numberOfProbes+1) % size;//linear probing
                numberOfProbes++;
            }
            HashTable[hashedKey][0] = keys[i];
            HashTable[i][1] = numberOfProbes; //stores number of probes in the 2nd column
            numberOfProbes = 0; //resetting number of probes
        }
        PrintResults(HashTable);
    }
    public static void HF2(int[] keys, int[][] HashTable)
    {
        int hashedKey; int numberOfProbes = 0;
        int size = HashTable.length;
        for(int i = 0; i < size; i ++)
        {
            hashedKey = keys[i] % size;
            while (HashTable[hashedKey][0] > 0) //check if empty condition
            {
                hashedKey = (int) (keys[i] + Math.pow(numberOfProbes+1,2))%size;//quadratic probing
                numberOfProbes++;
            }
            HashTable[hashedKey][0] = keys[i];
            HashTable[i][1] = numberOfProbes;
            numberOfProbes = 0;
        }
        PrintResults(HashTable);
    }
    /* REFERENCE FROM ASSIGNMENT
    H2 (key) = 30 – key % 25;
    Increment is { key + j * H2 (key) } % 50
    */
    public static void HF3(int[] keys, int[][] HashTable)
    {
        int hashedKey; int numberOfProbes = 0;
        int size = HashTable.length;
        for(int i = 0; i < size; i ++)
        {
            boolean isHashed = true;
            hashedKey = keys[i] % size;
            while (HashTable[hashedKey][0] > 0) //check if empty condition
            {
                hashedKey = (keys[i] + (numberOfProbes+1) * (30 - keys[i] % 25)) % size;
                if (numberOfProbes > 50) // no more than 50 tries
                {
                    System.out.println("Unable to hash " + keys[i] + " to the table");
                    numberOfProbes = 0; //resetting number of probes to not affect results
                    isHashed = false;//unsuccessful hash
                    break;
                }
                numberOfProbes++;
            }
            if (isHashed)//successfully hashed
            {
                HashTable[hashedKey][0] = keys[i];
                HashTable[i][1] = numberOfProbes;
            }
            numberOfProbes = 0;
        }
        PrintResults(HashTable);
    }
/*
        Hash function for HF4:
        Step One:
        If index is 0: Add Key[0]+Key[length-1] then % length
        Else Add Key[i]+Key[i-1] then % length
        Step Two:
        If hashed key is even: add it into the table
        else multiply it by 2 and add 1, then add it into the table

        Separate Chaining is implemented to handle collisions.
 */
    public static void HF4(int[] keys, int[][] HashTable)
    {
        int hashedKey;
        int size = HashTable.length;
        LinkedList<Integer> [] Buckets = new LinkedList[50]; //used for separate chaining
        for (int i = 0; i < Buckets.length; i++)
        {
            Buckets[i] = new LinkedList<>(); //initializing buckets for collision handling
        }
        for(int i = 0; i < size; i ++) //Hashing Method
        {
            if (i > 0) hashedKey = (keys[i]+keys[i-1]) % size;
            else hashedKey = (keys[i]+keys[i+keys.length-1] )% size;

            if (hashedKey%2 == 0) Buckets[hashedKey].add(keys[i]);
            else if (hashedKey %2 == 1)
            {
                hashedKey *=2 +1;
                hashedKey %=50;
                Buckets[hashedKey].add(keys[i]);
            }
        }
        for (int i = 0; i < size; i++) //Emptying buckets into HashTable
        {
            for (int j = 0; j < Buckets[i].toArray().length; j++)
            {
                HashTable[i][0] = Buckets[i].get(j);
            }
            //These two if statements keep track of number of probes:
            if (Buckets[i].size() >= 1)
                HashTable[i][1] = Buckets[i].size();
            if (Buckets[i].size() == 0)
                HashTable[i][1] = 0;
        }
        PrintResults(HashTable, Buckets);
    }
    public static void PrintResults(int[][] HashTable) // Used to print out index / key / #probes
    {
        System.out.println("\tIndex Key probes\n" +
                "\t---------------");
        for (int i = 0; i < HashTable.length; i++)
        {
            System.out.println("\t"+i + " " + HashTable[i][0] +" "+ HashTable[i][1]);
        }
        ProbeSum(HashTable);
    }
    public static void PrintResults(int[][] HashTable, LinkedList<Integer> [] Buckets) //This will Print HF4()
    {
        //Print statement includes all keys in each index aswell as the number of probes
        for (int i = 0; i < HashTable.length; i++)
        {
            System.out.print("\nIndex\t"+i + " contains: ");
            for (int j = 0; j < Buckets[i].size(); j++)
             System.out.print(Buckets[i].get(j) + ", ");
            System.out.print(" Probes: "+ HashTable[i][1]);
        }
        System.out.println();
        ProbeSum(HashTable);
    }
    public static void ProbeSum(int[][] HashTable) //returns # of probes
    {
        int sum=0;
        for (int[] ints : HashTable) {
            sum += ints[1];
        }
        System.out.println("Sum of Probes: " + sum);
    }

}