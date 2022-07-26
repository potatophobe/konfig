import ru.potatophobe.konfig.generated.application
import ru.potatophobe.konfig.generated.properties
import ru.potatophobe.konfig.generated.propertiesMap
import ru.potatophobe.konfig.generated.property

application {
    property {
        value = "1"
    }
    properties {
        element {
            value = "1"
        }
        element {
            value = "2"
        }
    }
    propertiesMap {
        key("1") value {
            value = "1"
        }
        key("2") value {
            value = "2"
        }
    }
}
