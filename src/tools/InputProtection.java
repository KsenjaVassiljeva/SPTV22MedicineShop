/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.Scanner;

/**
 *
 * @author admin
 */
public class InputProtection {
    public static int intInput(Integer beginRange, Integer endRange){
        
        Scanner scanner = new Scanner(System.in);
        int number = -1;
        do{
            try {
                number = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.print("Please enter a number: ");
                continue;
            }
            if(endRange == null){
                if(number >= beginRange){
                    return number;
                }else{
                    System.out.printf("Please enter a number > %d): ",beginRange);
                    continue;
                }
            }else if(number >= beginRange && number <= endRange){
                return number;
            }else{
                System.out.printf("Please enter a number (%d .. %d): ",beginRange, endRange);
                continue;
            }
        }while(true);
        
    }
}
