import java.io.UnsupportedEncodingException;

/**
 * To change this template use File | Settings | File Templates.
 *
 * @author wangqing
 * @since 16-2-23 上午9:54
 */
public class Test {




    public static void main(String[] args) throws UnsupportedEncodingException {
//        File file=new File("E:\\证件\\我是中国馆.txt");
//
//        String abPath=file.getAbsolutePath();
//        System.out.println(abPath);
//
//        System.out.println(file.getName());
//
//        String newFileName=abPath.substring(0,abPath.lastIndexOf("."));
//        String sufix=abPath.substring(abPath.lastIndexOf(".")+1);
//
//
//        System.out.println(newFileName);
//        System.out.println(sufix);
//
//        String name=file.getName();
//        String newName= Base64Utils.encode(name);
//        System.out.println(newName);
//        String folder=abPath.replace(file.getName(),"");
//
//        System.out.println(folder);

//        File directory = new File("");//设定为当前文件夹
//
//        System.out.println(directory.getAbsolutePath());

//        String path = System.getProperty("user.dir");
//        System.out.println("current diretory:" + path);

//         long t3=1000L/1000L;
//
//        System.out.println(t3);

        String str="D:\\download\\考核表（解释版）\\钢构车间\\复合板普工核表.xls";

        String h = "";

        byte[] buffer = str.getBytes("utf-8");
        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            h = h + " "+ temp;
        }

        System.out.println(h);


    }
}
