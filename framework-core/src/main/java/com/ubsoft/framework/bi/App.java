package com.ubsoft.framework.bi;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        String [] ss=new String[]{"fff"};
        if(ss instanceof Object[]){
        	Object[] boj=(Object[])ss;
        	System.out.print(boj[0]);
        }
    }
}
