import net.playblack.cuboids.Bootstrapper;
import net.playblack.cuboids.converters.Loader;


public class Cuboids2 extends Plugin {

    @Override
    public void disable() {
    }

    @Override
    public void enable() {
        Loader[] loaders = new Loader[] {new CuboidDLoader()};
        new Bootstrapper(new CanaryServer(), loaders);
    }

}
