SUMMARY = "An HTTP library implementation in C"
HOMEPAGE = "https://wiki.gnome.org/Projects/libsoup"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "x11/gnome/libs"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

DEPENDS = "glib-2.0 glib-2.0-native libxml2 sqlite3 intltool-native"

SHRT_VER = "${@d.getVar('PV').split('.')[0]}.${@d.getVar('PV').split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/libsoup/${SHRT_VER}/libsoup-${PV}.tar.xz \
           file://CVE-2018-12910.patch"
SRC_URI[md5sum] = "eaf99b04ac8968ed2b26f2509ba75584"
SRC_URI[sha256sum] = "9e536fe3da60b25d2c63addb84a9d5072d00b0d8b8cbeabc629a6bcd63f879b6"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools gettext pkgconfig upstream-version-is-even gobject-introspection gtk-doc

# libsoup-gnome is entirely deprecated and just stubs in 2.42 onwards. Disable by default.
PACKAGECONFIG ??= ""
PACKAGECONFIG[gnome] = "--with-gnome,--without-gnome"
PACKAGECONFIG[gssapi] = "--with-gssapi,--without-gssapi,krb5"

EXTRA_OECONF = "--disable-vala"

# When built without gnome support, libsoup-2.4 will contain only one shared lib
# and will therefore become subject to renaming by debian.bbclass. Prevent
# renaming in order to keep the package name consistent regardless of whether
# gnome support is enabled or disabled.
DEBIAN_NOAUTONAME_${PN} = "1"

# glib-networking is needed for SSL, proxies, etc.
RRECOMMENDS_${PN} = "glib-networking"
