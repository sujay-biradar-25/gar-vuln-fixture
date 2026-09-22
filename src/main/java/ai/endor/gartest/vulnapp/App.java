package ai.endor.gartest.vulnapp;

import ai.endor.gartest.vulnlib.VulnSurface;

/**
 * Calls every wrapper in the private GAR package. main is the call-graph entry
 * point, so each vulnerable third-party sink sits two hops away: App.main ->
 * VulnSurface.x -> the vulnerable library call.
 */
public final class App {

  public static void main(String[] args) throws Exception {
    String input = args.length > 0 ? args[0] : "default";

    VulnSurface.logUserInput(input);
    VulnSurface.interpolate(input);
    VulnSurface.invokeTransform(input, "toString");
    VulnSurface.detectArchive(new byte[] {0x50, 0x4b, 0x03, 0x04});
    VulnSurface.fetch("https://example.com");
  }

}
