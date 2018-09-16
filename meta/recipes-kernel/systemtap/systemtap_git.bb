SUMMARY = "Script-directed dynamic tracing and performance analysis tool for Linux"
HOMEPAGE = "https://sourceware.org/systemtap/"

require systemtap_git.inc

SRC_URI += "file://0001-improve-reproducibility-for-c-compiling.patch"

DEPENDS = "elfutils"

EXTRA_OECONF += "--with-libelf=${STAGING_DIR_TARGET} --without-rpm \
            --without-nss --without-avahi --without-dyninst \
            --disable-server --disable-grapher --enable-prologues \
            --with-python3 --without-python2-probes \
            ac_cv_prog_have_javac=no \
            ac_cv_prog_have_jar=no "

STAP_DOCS ?= "--disable-docs --disable-publican --disable-refdocs"

EXTRA_OECONF += "${STAP_DOCS} "

PACKAGECONFIG ??= "translator sqlite monitor python3-probes"
PACKAGECONFIG[translator] = "--enable-translator,--disable-translator,boost,python3-core bash perl"
PACKAGECONFIG[libvirt] = "--enable-libvirt,--disable-libvirt,libvirt"
PACKAGECONFIG[sqlite] = "--enable-sqlite,--disable-sqlite,sqlite3"
PACKAGECONFIG[monitor] = "--enable-monitor,--disable-monitor,ncurses json-c"
PACKAGECONFIG[python3-probes] = "--with-python3-probes,--without-python3-probes,python3-setuptools-native"

inherit autotools gettext pkgconfig distutils3-base

do_configure_prepend () {
    # Improve reproducibility for c++ object files
    reltivepath="${@os.path.relpath(d.getVar('STAGING_INCDIR'), d.getVar('S'))}"
    sed -i "s:@RELATIVE_STAGING_INCDIR@:$reltivepath:g" ${S}/stringtable.h
}

do_install_append () {
   if [ ! -f ${D}${bindir}/stap ]; then
      # translator disabled case, need to leave only minimal runtime
      rm -rf ${D}${datadir}/${PN}
      rm ${D}${libexecdir}/${PN}/stap-env
   fi
}

BBCLASSEXTEND = "nativesdk"