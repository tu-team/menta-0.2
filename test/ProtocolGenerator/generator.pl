#!/usr/bin/perl
use strict;
use warnings;

require 'process_files.pl';
require 'createProtocolFromTemplate.pl';
require 'getClassGetSuperClass.pl';

my @x = process_files ("E:\\Workspace\\menta\\Menta\\model\\src\\main\\scala\\menta\\model");
foreach (@x) {
	if ($_ =~ m/.*?\.scala/ and $_ !~ m/.*?Protocol.*?/)
	{
 		print $_."\n";
		my ($one, $two) = get_two($_);
		createProtocolFromTemplate("ProtocolTemplate.tml", $one, $two);
		
	}
} 
