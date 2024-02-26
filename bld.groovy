@Grapes(
        @Grab(group='com.uwyn.rife2', module='bld', version='1.9.0')
)

import static rife.bld.dependencies.Repository.*
import rife.bld.dependencies.Scope
import static rife.bld.operations.TemplateType.HTML

def proj = BuildConfig.webProject() {
    pkg = "com.example"
    name = 'Mywebapp'
    mainClass = "com.example.MywebappSite"
    uberJarMainClass = "com.example.MywebappSiteUber"
    version = version(0,1,0)
    downloadSources = true
    repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES)

    compile() {
        include() {
            dependency("com.uwyn.rife2", "rife2", version(1,7,3))
        }
    }
    test() {
        include() {
            dependency("org.jsoup", "jsoup", version(1,17,2))
            dependency("org.junit.jupiter", "junit-jupiter", version(5,10,2))
            dependency("org.junit.platform", "junit-platform-console-standalone", version(1,10,2))
        }
    }
    standalone() {
        include() {
            dependency("org.eclipse.jetty.ee10", "jetty-ee10", version(12,0,6))
            dependency("org.eclipse.jetty.ee10", "jetty-ee10-servlet", version(12,0,6))
            dependency("org.slf4j", "slf4j-simple", version(2,0,11))
        }
    }
    precompileOperation() {
        templateTypes(HTML)
    }
}

proj.start(args)

// -------------------------------------------------------------------------------------------
// All below here would be in a library no the bld.groovy file for ultimate end-user realities

class BuildConfig {
    def static webProject(Closure config) {
        def proj = new GldWebProject()
        config.delegate = proj
        config.resolveStrategy = Closure.DELEGATE_FIRST
        config()
        return proj
    }
}
class GldWebProject extends rife.bld.WebProject {
    def compile(Closure config) {
        def gldScope = new GldScope(this, scope(Scope.compile))
        config.delegate = gldScope
        config.resolveStrategy = Closure.DELEGATE_FIRST
        config()
    }
    def test(Closure config) {
        def gldScope = new GldScope(this, scope(Scope.test))
        config.delegate = gldScope
        config.resolveStrategy = Closure.DELEGATE_FIRST
        config()
    }
    def standalone(Closure config) {
        def gldScope = new GldScope(this, scope(Scope.standalone))
        config.delegate = gldScope
        config.resolveStrategy = Closure.DELEGATE_FIRST
        config()

    }
    def precompileOperation(Closure config) {
        def gldpo = new GldPrecompileOperation(this)
        config.delegate = gldpo
        config.resolveStrategy = Closure.DELEGATE_FIRST
        config()
    }
}
class GldScope {
    def proj
    def sc
    def GldScope(rife.bld.WebProject proj, rife.bld.dependencies.DependencySet scope) {
        this.sc = scope
        this.proj = proj
    }
    def include(Closure config) {
        def gldinclude = new GldInclude(proj, sc)
        config.delegate = gldinclude
        config.resolveStrategy = Closure.DELEGATE_FIRST
        config()
    }

}
class GldInclude {
    def proj
    def ds
    def GldInclude(rife.bld.WebProject proj, rife.bld.dependencies.DependencySet scope) {
        this.proj = proj
        this.ds = scope
    }
    def dependency(g, a, v) {
        ds.include(proj.dependency(g, a, v))
    }
}
class GldPrecompileOperation {
    def proj
    def ttype
    def GldPrecompileOperation(rife.bld.WebProject proj) {
        this.proj = proj
    }
    def templateTypes(rife.bld.operations.TemplateType ttype) {
        this.ttype = ttype
        proj.precompileOperation().templateTypes(ttype)
    }
}