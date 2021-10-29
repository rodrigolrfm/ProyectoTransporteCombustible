public class Chromosome {
    int count;
    int sum;
    int[] particion;

    public Chromosome(int count, int sum){
        this.count = count;
        this.sum = sum;
    }

    public void genrateRandomChromosome(){
        java.util.Random g = new java.util.Random();

        particion = new int[count];
        sum -= count;

        if(sum == 0){
            for (int i = 0; i < count; ++i) { particion[i] = 1; }
        }else{

            for (int i = 0; i < count-1; ++i) {
                particion[i] = g.nextInt(sum);
            }
            particion[count-1] = sum;

            java.util.Arrays.sort(particion);
            for (int i = count-1; i > 0; --i) {
                particion[i] -= particion[i-1];
            }
            for (int i = 0; i < count; ++i) { ++particion[i]; }
        }
    }

    public void print(){
        for (int i = 0; i < count; ++i) {
            System.out.printf("%4d", particion[i]);
        }
        System.out.printf("\n");
    }
}
