package com.premiummobile.First.util;

public class Test {

	public static void main(String[] args) {
		for(int i = 300; i <= 1000; i++){
			if(i % 10 == 0){
				System.out.println(i-1);
			}
			else{
				if(i % 10 > 5){
					System.out.println(i- (i % 10) + 9);
				}
				else{
					System.out.println(i- (i % 10) - 1);
				}
			}
		}
	}
}
