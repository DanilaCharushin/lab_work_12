public class SmallBirdFactory implements BirdFactory {
    @Override
    public Bird createBird(int cordX, int cordY) {
        return new SmallBird(cordX, cordY);
    }
}
