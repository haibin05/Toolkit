import java.io.*;

/**
 * Created by haibin on 2016/7/3.
 */
public class RemoveCommentsFromJavaFile {

    public static void main(String[] args) throws IOException {
        removeComments("D:\\workspace\\vehicle\\api\\src\\main\\java\\com\\yunguchang", "E:\\vehicle\\api\\src\\main\\java\\com\\yunguchang"); // 这里写好源文件夹和目的文件夹
        System.out.println("ok");
    }

    private static void removeComments(String srcPath, String descPath) throws IOException {
        removeComments(new File(srcPath), new File(descPath));
    }

    private static void removeComments(File srcFile, File descFile) throws IOException {
        if (srcFile.isFile()) { // 文件
            File parent = descFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs(); // 创建文件夹
            }
            if (srcFile.getName().endsWith(".java")) {
                removeCommentFromJavaFile(srcFile, descFile);
            } else {
                ignoreWithOutCommentFileByCopyOperation(srcFile, descFile);
            }
        } else { // 文件夹
            for (File file : srcFile.listFiles()) {
                // 相对路径
                String srcPath = file.getAbsolutePath().substring(srcFile.getAbsolutePath().length());
                removeComments(file, new File(descFile.getAbsolutePath() + srcPath));
            }
        }
    }

    private static void removeCommentFromJavaFile(File srcJavaFile, File descJavaFile) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcJavaFile), "utf-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descJavaFile), "utf-8"));
        StringBuffer fileText = new StringBuffer(1000*1000);

        String line = br.readLine();
        do {
            fileText.append(line);
            fileText.append("\n");
            line = br.readLine();
        } while (line != null);

        String fileTextStr = fileText.toString();
        fileTextStr = fileTextStr.replaceAll("(\\s*//.*)|(\\s*/\\*(.|\\n|\\r\\n|\\r)*?\\*/)", "");
        bw.write(fileTextStr);
        br.close();
        bw.close();
    }

    private static void ignoreWithOutCommentFileByCopyOperation(File srcGeneralFile, File descGeneralFile) throws IOException {
        OutputStream output = new FileOutputStream(descGeneralFile);
        InputStream input = new FileInputStream(srcGeneralFile);
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        input.close();
        output.close();
    }
}
