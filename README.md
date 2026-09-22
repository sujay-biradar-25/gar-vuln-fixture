# gar-vuln-fixture

Test fixture for Endor Labs scans of a project whose only direct dependency comes
from a private Google Artifact Registry repository.

**The dependencies here are deliberately vulnerable. Do not use this code.**

## What it is

`vulnapp` depends on one package, `ai.endor.gartest:vulnlib`, which is published
to a private GAR Maven repository and is not available from Maven Central. The
library wraps vulnerable entry points in several OSS packages, and `App.main`
calls every wrapper, so each vulnerable function sits two hops from an entry
point:

```
App.main -> VulnSurface.<wrapper> -> the vulnerable library call
```

That structure is what makes the findings reachable rather than merely declared.

## Expected result

A scan reports **11 reachable findings**, spread across log4j-core 2.14.1,
commons-collections 3.2.1, commons-compress 1.20, httpclient 4.5.12 and
commons-text 1.9.

The count moves in steps, not one at a time, because a single pinned version
carries a whole block of advisories. An earlier revision that also pinned
jackson-databind 2.9.10.1 and xstream 1.4.15 reported 83.

## Reproducing

1. Publish `vulnlib` to a GAR Maven repository and point the `artifact-registry`
   repository in `pom.xml` at it.
2. Create a `PackageManager` in your Endor namespace with a GAR auth provider for
   that repository, so the scan can authenticate.
3. If your `~/.m2/settings.xml` has a mirror with `mirrorOf=*`, exclude the
   private repo or Maven will route the request away from Artifact Registry:

   ```xml
   <mirrorOf>*,!artifact-registry</mirrorOf>
   ```

4. Scan:

   ```bash
   endorctl -n <namespace> scan
   ```

Build with JDK 17. Newer JDKs fail the endorctl host check.
