public class array_vs_arraylist {
    public static void main(String[] args) {
    int [] nums={1,2,3};
    System.out.println(nums[1]);
    for (int n : nums)
    {
        System.out.println(n+" ");
    }
}
}
/*{
        // Using an array
        String[] fruitsArray = new String[3];
        fruitsArray[0] = "Apple";
        fruitsArray[1] = "Banana";
        fruitsArray[2] = "Cherry";

        System.out.println("Array elements:");
        for (String fruit : fruitsArray) {
            System.out.println(fruit);
        }

        // Using an ArrayList
        java.util.ArrayList<String> fruitsList = new java.util.ArrayList<>();
        fruitsList.add("Apple");
        fruitsList.add("Banana");
        fruitsList.add("Cherry");
        fruitsList.add("Date"); // Dynamic resizing

        System.out.println("\nArrayList elements:");
        for (String fruit : fruitsList) {
            System.out.println(fruit);
        }
    } */