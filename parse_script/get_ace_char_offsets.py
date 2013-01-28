#!/usr/bin/env python
''' For every .sgm file in the ACE 2004 corpus, determines the
    character offset at the beginning of the file. So to get the character
    offsets of the annotated entities, you'll add the value in the CoreNLP
    output to this offset (and probably subtract 1, because CoreNLP indices
    are 1-indexed, while ACE offsets are 0-indexed)
'''

import fnmatch
import optparse
import os
import re
import sys


def main(inbase, outbase):
  data_dir = os.path.join(os.path.join(inbase, 'data'), 'English')
  for dirname in os.listdir(data_dir):
    absdir = os.path.join(data_dir, dirname)
    if not os.path.isdir(absdir):
      continue
    for fname in os.listdir(absdir):
      if fnmatch.fnmatch(fname, '*.sgm'):
        outfname = os.path.join(outbase, fname.replace('.sgm', '.offset'))
        offset = get_offset(os.path.join(absdir, fname))
        f = open(outfname, 'w')
        f.write('%d\n' % offset)
        f.close()

def get_offset(sgmfile):
  m = re.search(r'(.*)<TEXT>(\s*)(.*)</TEXT>',
                open(sgmfile, 'r').read(), re.DOTALL)
  assert m
  return len(remove_sgm_tags(m.group(1))) + len(m.group(2))

def remove_sgm_tags(pretext):
  cur = []
  in_tag = False
  for ch in pretext:
    if ch == '<':
      in_tag = True
    if not in_tag:
      cur.append(ch)
    if ch == '>' and in_tag:
      in_tag = False
  return ''.join(cur)

def get_parser():
  p = optparse.OptionParser()
  p.add_option("--base_of_ace", action="store", type="string",
               dest="base_of_ace", default=None,
               help="the base directory of the ACE 2004 data set. Required.")
  p.add_option("--outbase", action="store", type="string",
               dest="outbase", default=None,
               help="the base directory of the output files. Required.")
  return p


if __name__ == '__main__':
  op = get_parser()
  (opts, args) = op.parse_args()
  if not (opts.base_of_ace and opts.outbase):
    sys.stderr.write('More Options required. Call with --help for help.\n')
    sys.exit(1)
  main(opts.base_of_ace, opts.outbase)
