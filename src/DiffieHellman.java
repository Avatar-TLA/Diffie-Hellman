import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class DiffieHellman {
    private final static SecureRandom random = new SecureRandom();
    private BigInteger q;
    private BigInteger a;

    private static class User{
        private BigInteger privatekey;
        private BigInteger publickey;
    }

    private DiffieHellman(int N) {
        q = BigInteger.probablePrime(N, random);
        a = new BigInteger("3");//else 65537
//        randomization of a
//        while(a.compareTo(q)==1 || a.compareTo(q)==0) {
//            a = BigInteger.probablePrime(N, random);
//        }
    }

    private static void CreatePublicKey(User user, BigInteger q, BigInteger a){
        int N;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter key bit number:");
        N = input.nextInt();
        BigInteger privatekey = new BigInteger(N, random);
        while(privatekey.compareTo(q) == 1 || privatekey.compareTo(q) == 0) {
            System.out.print("Too big! Enter key bit number:");
            N = input.nextInt();
            privatekey = new BigInteger(N, random);
        }
        BigInteger publickey = a.modPow(privatekey,q);
        user.privatekey = privatekey;
        user.publickey = publickey;
    }

    private static BigInteger CreatePrivateKey(BigInteger publickey, BigInteger privatekey, BigInteger q){
        return publickey.modPow(privatekey,q);
    }

    private static void CrackKey(BigInteger q, BigInteger a, BigInteger ya, BigInteger yb){
        BigInteger counter = new BigInteger("0");
        BigInteger key1 = new BigInteger("-1");
        BigInteger key2 = new BigInteger("-1");
        System.out.println("Public information i know");
        System.out.println("q:" + q);
        System.out.println("a:" + a);
        System.out.println("Ya:" + ya);
        System.out.println("Yb:" + yb);
        long start = System.nanoTime();
        while(counter.compareTo(q) < 0){
            if(key1.compareTo(ya) == 0){
                break;
            }
            else {
                counter = counter.add(BigInteger.ONE);
                key1 = a.modPow(counter, q);
            }
        }
        //System.out.println("Partial key1 found:" + key1 );
        key1 = yb.modPow(counter, q);
        System.out.println("Key found:" + key1 );
        long elapsedTime = System.nanoTime() - start;
        System.out.println("First key cracked in:" + elapsedTime/1000000000.0 + " second");
        /*start = System.nanoTime();
        counter = new BigInteger("0");
        while(counter.compareTo(q) < 0){
            if(key2.compareTo(yb) == 0){
                break;
            }
            else {
                counter = counter.add(BigInteger.ONE);
                key2 = a.modPow(counter, q);
            }
        }
        System.out.println("Partial key2 found:" + key2 );
        key2 = ya.modPow(counter, q);
        elapsedTime = System.nanoTime() - start;
        System.out.println("Second key cracked in:" + elapsedTime/1000000000.0 + " second");
        if(key1.compareTo(key2) == 0){
            System.out.println("Shared private key is:" + key1);
        }
        else{
            System.out.println("Something went bad!");
        }*/
    }

    public static void main(String[] args) {
        int menu;
        DiffieHellman key = null;
        User user1 = null, user2 = null;
        Scanner input = new Scanner(System.in);
        System.out.println("~Welcome to Diffie-Hellman application~");
        while(true) {
            System.out.println("Menu");
            System.out.println("1.Diffie-Hellman initiation values");
            System.out.println("2.Diffie-Hellman user1 keys creation");
            System.out.println("3.Diffie-Hellman user2 keys creation");
            System.out.println("4.Diffie-Hellman user1 common key with user2");
            System.out.println("5.Diffie-Hellman user2 common key with user1");
            System.out.println("0.Exit");
            menu = input.nextInt();
            switch(menu) {
                case 0:
                    break;
                case 1:
                    System.out.println("Enter the number of bits");
                    int N = input.nextInt();
                    key = new DiffieHellman(N);
                    System.out.println("~Public Information~");
                    System.out.println("q:" + key.q);
                    System.out.println("a:" + key.a);
                    break;
                case 2:
                    user1 = new User();
                    System.out.println("Create keys for user1.");
                    CreatePublicKey(user1, key.q, key.a);
                    System.out.println("Your private key:" + user1.privatekey);
                    System.out.println("Your public key:" + user1.publickey);
                    break;
                case 3:
                    user2 = new User();
                    System.out.println("Create keys for user2.");
                    CreatePublicKey(user2, key.q, key.a);
                    System.out.println("Your private key:" + user2.privatekey);
                    System.out.println("Your public key:" + user2.publickey);
                    break;
                case 4:
                    BigInteger secretA = CreatePrivateKey(user2.publickey, user1.privatekey, key.q);
                    System.out.println("SecretA is:" + secretA);
                    break;
                case 5:
                    BigInteger secretB = CreatePrivateKey(user1.publickey, user2.privatekey, key.q);
                    System.out.println("SecretB is:" + secretB);
                    break;
                case 6:
                    CrackKey(key.q, key.a, user1.publickey, user2.publickey);
                    break;
                default:
                    System.out.println("Wrong Input.");
                    break;
            }
            if(menu==0) {
                break;
            }
        }
    }
}
