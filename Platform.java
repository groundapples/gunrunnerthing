public class Platform extends Wall {
    Platform(int x, int y, int length, int width, String picName) {
        super(x, y, length, width, picName);
        super.setType("Platform");
    }
}