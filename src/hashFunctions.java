
import java.util.Scanner;
public class hashFunctions
{
    public static void main(String[] args){
        int[] keys =
                {1234, 8234, 7867, 1009, 5438, 4312, 3420, 9487, 5418, 5299,
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
                case "1" -> HF1(keys, HashTable);
                case "2" -> HF2(keys, HashTable);
                case "3" -> HF3(keys, HashTable);
                case "4", "5" -> isFinished = true;
                default -> System.out.println("Enter valid input");
            }
        }

    }
    public static void DisplayMenu()
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
            hashedKey = keys[i] % size;
            while (HashTable[hashedKey][0] > 0) //check if empty condition
            {
                hashedKey = (keys[i] + numberOfProbes+1) % size;//resets hashedKey to 0 to avoid out-of-bounds
                numberOfProbes++;
            }
            HashTable[hashedKey][0] = keys[i];
            HashTable[i][1] = numberOfProbes;
            numberOfProbes = 0;
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
                hashedKey = (int) (keys[i] + Math.pow(numberOfProbes+1,2))%size;
                numberOfProbes++;
            }
            HashTable[hashedKey][0] = keys[i];
            HashTable[i][1] = numberOfProbes;
            numberOfProbes = 0;
        }
        PrintResults(HashTable);
    }
    /* Specifically, double hashing looks at the cells at indices (k + j* h′(key)) % N, for j Ú 0, that is,
k % N, (k + h′(key))% N, (k + 2* h′(key)) % N, (k + 3* h′(key)) % N, and so on

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
                    numberOfProbes = 0;
                    isHashed = false;
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
    public static void PrintResults(int[][] HashTable)
    {
        System.out.println("\tIndex Key probes\n" +
                "\t---------------");
        for (int i = 0; i < HashTable.length; i++)
        {
            System.out.println("\t"+i + " " + HashTable[i][0] +" "+ HashTable[i][1]);
        }
        ProbeSum(HashTable);
    }
    public static void ProbeSum(int[][] HashTable)
    {
        int sum=0;
        for (int i = 0; i < HashTable.length; i++)
        {
            sum+=HashTable[i][1];
        }
        System.out.println("Sum of Probes: " + sum);
    }

}