public class BigBirdFactory implements BirdFactory {
    @Override
    public Bird createBird(int cordX, int cordY) {
        return new BigBird(cordX, cordY);
    }
}
