import java.io.*;

public class MyClassLoader extends ClassLoader{
    private String pathToFile;

    public MyClassLoader(String pathToFile, ClassLoader parent){
        super(parent);
        this.pathToFile = pathToFile;
    }
    @Override
    public Class<?> findClass(String classname) throws ClassNotFoundException{
        try {

            //Получаем байт код и загружаем в рантайм
            byte b[] = takeClassFromFS(pathToFile + classname + ".class");
            return defineClass(classname,b,0,b.length);
        } catch (FileNotFoundException e) {
            return super.findClass(classname);
        } catch (IOException e) {
            return super.findClass(classname);
        }

    }

    private byte[] takeClassFromFS(String path) throws FileNotFoundException, IOException{
        InputStream inputStream = new FileInputStream(new File(path));

        //Получаем размер файла
        long length = new File(path).length();

        if(length > Integer.MAX_VALUE){
            System.out.println("Этот файл слишком большой!");
        }

        //Массив для хранения байтов
        byte[] bytes = new byte[(int) length];

        //Начинаем читать байты
        int offSet = 0;
        int numRead = 0;
        while(offSet < bytes.length && (numRead=inputStream.read(bytes, offSet, bytes.length-offSet)) >= 0){
            offSet +=numRead;
        }
        //Проверка, что все байты были считаны
        if(offSet < bytes.length){
            throw new IOException("Не могу прочитать файл" + path);
        }
        inputStream.close();
        return bytes;
    }


}
